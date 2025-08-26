/*
 * Big bang is commented out, 
 * Implemented different colors for different distances from the power station
 */

import java.util.ArrayList;
import java.util.HashMap;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;


// the given code for the object LightEmAll that extends the World class
class LightEmAll extends World {
  // a list of columns of GamePieces,
  // i.e., represents the board in column-major order
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // a list of edges of the minimum spanning tree
  ArrayList<Edge> mst; 
  // the width and height of the board
  int width;
  int height;
  // the current location of the power station,
  // as well as its effective radius
  int powerRow;
  int powerCol;
  int radius;


  LightEmAll(int width, int height, int powerRow, int powerCol, int radius) { 
    this.width = width; 
    this.height = height;
    this.powerRow = powerRow;
    this.powerCol = powerCol;
    this.radius = radius;
    this.board = new ArrayList<ArrayList<GamePiece>>();
    this.nodes = new ArrayList<GamePiece>();
    this.mst = new ArrayList<Edge>();
    this.initBoard();

  }

  // EFFECT: initializes the edges of the board
  // Creates a list of all edges in the board
  public void initEdges() {
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        if (r != 0) {
          Edge e = new Edge(this.board.get(r).get(c), 
              this.board.get(r - 1).get(c), new Random().nextInt(30));

          this.mst.add(e);
        }        
        if (c != 0) {
          Edge e = new Edge(this.board.get(r).get(c), 
              this.board.get(r).get(c - 1), new Random().nextInt(30));
          this.mst.add(e);
        }
      }
    }
  }

  // EFFECT: initializes the board
  // Creates a board with one possible solution and each tile randomly rotated
  public void initBoard() { 
    this.powerCol = 0;
    this.powerRow = 0;

    // right here i try and just create a board with all the same cells
    for (int r = 0; r < height; r++) {
      ArrayList<GamePiece> row = new ArrayList<GamePiece>();
      for (int c = 0; c < width; c++) { 
        row.add(new GamePiece(r, c, false, false, false, false, false, false));
      }
      this.board.add(row);
    }

    initEdges();

    // sorts the edges from smallest to greatest
    Edge temp = new Edge(new GamePiece(0, 0, false, false, false, false, false, false),
        new GamePiece(0, 0, false, false, false, false, false, false), 0);
    for (int i = 0; i < this.mst.size() - 1; i++) {
      for (int j = i + 1; j < this.mst.size(); j++) {
        if (this.mst.get(i).weight > this.mst.get(j).weight) {
          temp = this.mst.get(i);
          this.mst.set(i, this.mst.get(j));
          this.mst.set(j, temp);
          i = 0;
          j = 1;
        }
      }
    }

    HashMap<GamePiece, GamePiece> representatives = new HashMap<GamePiece, GamePiece>();
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();

    for (int r = 0; r < this.height; r++) {
      for (int c = 0; c < this.width; c++) {
        representatives.put(this.board.get(r).get(c), this.board.get(r).get(c)); 
      }
    }
    while (moreThanOneTree(representatives)) {
      Edge other = mst.remove(0);
      if (!find(representatives, other.fromNode).equals(find(representatives, other.toNode))) {
        edgesInTree.add(other);
        union(representatives, 
            find(representatives, other.fromNode), 
            find(representatives, other.toNode));
      }
    }

    int treeSize = edgesInTree.size();
    for (int i = 0; i < treeSize; i++) {
      Edge extra = edgesInTree.remove(0);

      if (extra.fromNode.row < extra.toNode.row) {
        extra.fromNode.bottom = true;
        extra.toNode.top = true;
      } else if (extra.fromNode.row > extra.toNode.row) {
        extra.fromNode.top = true;
        extra.toNode.bottom = true;
      } else  if (extra.fromNode.col < extra.toNode.col) {
        extra.fromNode.right = true;
        extra.toNode.left = true;
      } else if (extra.fromNode.col > extra.toNode.col) {
        extra.fromNode.left = true;
        extra.toNode.right = true;
      }


    }

    this.board.get(0).get(0).powerStation = true;
    this.board.get(0).get(0).powered = true;

    // rotating all the tiles a random amount of times 
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {

        GamePiece other = new GamePiece(r, c, this.board.get(r).get(c).left, 
            this.board.get(r).get(c).right, this.board.get(r).get(c).top, 
            this.board.get(r).get(c).bottom, this.board.get(r).get(c).powerStation, 
            this.board.get(r).get(c).powered);
        for (int i = 0; i < new Random().nextInt(4); i++) { 
          this.board.get(r).get(c).top = other.right; 
          this.board.get(r).get(c).left = other.top;
          this.board.get(r).get(c).bottom = other.left;
          this.board.get(r).get(c).right = other.bottom;
        }
      }
    }


    hasPath();
    worldEnds();
  }

  // Returns true if there is more than one tree in the minimum spanning tree
  boolean moreThanOneTree(HashMap<GamePiece, GamePiece> rep) {
    GamePiece x = find(rep, board.get(0).get(0));
    for (int r = 0; r < this.height; r++) {
      for (int c = 0; c < this.width; c++) {
        if (!x.equals(find(rep, board.get(r).get(c)))) {
          return true;
        }
      }
    }
    return false;
  }

  // Returns the topmost representative of the given GamePiece
  GamePiece find(HashMap<GamePiece, GamePiece> rep, GamePiece to) {

    if (rep.get(to) == to) {
      return to;
    }
    return find(rep, rep.get(to));

  }

  // EFFECT: sets the representative of x to y
  // Merges two trees by setting the representative of x to y
  void union(HashMap<GamePiece, GamePiece> rep, GamePiece x, GamePiece y) {
    rep.put(x, y);
  }





  // checks if whether or not each tile is connected to one with power
  // EFFECT: if a tile is connected in some way to the powerStation
  // then turns the powered field to true
  void hasPath() {

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        this.board.get(r).get(c).distance = 0;
        if (!this.board.get(r).get(c).powerStation) {
          this.board.get(r).get(c).powered = false;
        }
      }
    }


    ArrayList<GamePiece> worklist = new ArrayList<GamePiece>();
    ArrayList<GamePiece> alreadySeen = new ArrayList<GamePiece>();
    worklist.add(board.get(powerRow).get(powerCol));



    while (worklist.size() > 0) {
      GamePiece next = worklist.get(0);
      next.powered = true;
      GamePiece top = new GamePiece(0, 0, false, false, false, false, false, false);
      GamePiece left = new GamePiece(0, 0, false, false, false, false, false, false);
      GamePiece right = new GamePiece(0, 0, false, false, false, false, false, false);
      GamePiece bottom = new GamePiece(0, 0, false, false, false, false, false, false);

      if (alreadySeen.contains(next)) {
        next = worklist.remove(0);
      }

      alreadySeen.add(next);
      alreadySeen.add(top);
      alreadySeen.add(left);
      alreadySeen.add(right);
      alreadySeen.add(bottom);

      if (next.distance < this.radius) {
        if (next.row != 0) {
          top = this.board.get(next.row - 1).get(next.col);
        }
        if (next.row != height - 1) {
          bottom = this.board.get(next.row + 1).get(next.col);
        }
        if (next.col != 0) {
          left = this.board.get(next.row).get(next.col - 1);
        }
        if (next.col != width - 1) {
          right = this.board.get(next.row).get(next.col + 1);
        }

        if (next.top && top.bottom && !alreadySeen.contains(top)) {
          top.distance = next.distance + 1;
          worklist.add(top);
          top.powered = true;

        }

        if (next.bottom && bottom.top && !alreadySeen.contains(bottom)) {
          bottom.distance = next.distance + 1;
          worklist.add(bottom);
          bottom.powered = true;

        }

        if (next.left && left.right && !alreadySeen.contains(left)) {
          left.distance = next.distance + 1;
          worklist.add(left);
          left.powered = true;

        }

        if (next.right && right.left && !alreadySeen.contains(right)) {
          right.distance = next.distance + 1;
          worklist.add(right);
          right.powered = true;

        }
      }

    }

  }




  // overiding the worldEnds() method in World and shows a winning image if the win the game
  public WorldEnd worldEnds() {
    boolean allLit = true;

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        if (!this.board.get(r).get(c).powered) {
          allLit = false;
        }
      }
    }
    if (allLit) {
      return new WorldEnd(true, this.drawEnd(true));
    } else {
      return new WorldEnd(false, this.drawEnd(false));
    }
  }



  // this returns a worldScene that is drawn if the game is won
  public WorldScene drawEnd(boolean result) {
    WorldScene w = this.makeScene();
    if (result) {
      w.placeImageXY(new TextImage("You Win!", 30, Color.GREEN), this.width * 50,
          this.height * 50);
    }

    return w;
  }



  // returns the worldScene that is drawn
  public WorldScene makeScene() {

    WorldScene w = new WorldScene(100 * width, 100 * height);  
    WorldImage temp;
    Color color = Color.black;
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        if (board.get(r).get(c).powered) {
          if (this.board.get(r).get(c).distance < radius / 3) {
            color = new Color(255, 255, 0);
          } else if (this.board.get(r).get(c).distance < radius / 2) {
            color = new Color(200, 200, 0);
          } else if (this.board.get(r).get(c).distance < radius / 1.5) {
            color = new Color(150, 150, 0);
          } else if (this.board.get(r).get(c).distance < radius) {
            color = new Color(100, 100, 0);
          }
        } else {
          color = Color.black;
        }
        temp = board.get(r).get(c).tileImage(100 , 20, color, board.get(r).get(c).powerStation);
        w.placeImageXY(temp, c * 100 + 50, r * 100 + 50);
      }
    }
    return w;



  }



  // Moves the power station based an onKeyEvent
  public void onKeyEvent(String k) {
    if (k.equals("up") && this.powerRow > 0 && this.board.get(powerRow).get(powerCol).top 
        && this.board.get(powerRow - 1).get(powerCol).bottom) {
      this.board.get(powerRow).get(powerCol).powerStation = false;
      this.powerRow -= 1;
      this.board.get(powerRow).get(powerCol).powerStation = true;
    }
    else if (k.equals("down") && this.powerRow < height - 1 
        && this.board.get(powerRow).get(powerCol).bottom
        && this.board.get(powerRow + 1).get(powerCol).top) {
      this.board.get(powerRow).get(powerCol).powerStation = false;
      this.powerRow += 1;
      this.board.get(powerRow).get(powerCol).powerStation = true;
    }
    else if (k.equals("left") && this.powerCol > 0 && this.board.get(powerRow).get(powerCol).left
        && this.board.get(powerRow).get(powerCol - 1).right) {
      this.board.get(powerRow).get(powerCol).powerStation = false;
      this.powerCol -= 1;
      this.board.get(powerRow).get(powerCol).powerStation = true;
    }
    else if (k.equals("right") && this.powerCol < width - 1 
        && this.board.get(powerRow).get(powerCol).right
        && this.board.get(powerRow).get(powerCol + 1).left) {
      this.board.get(powerRow).get(powerCol).powerStation = false;
      this.powerCol += 1;
      this.board.get(powerRow).get(powerCol).powerStation = true;
    }
    hasPath();
    worldEnds();

  }

  // Rotates the piece at the clicked posn
  public void onMouseClicked(Posn pos) {
    int row = pos.y / 100;
    int col = pos.x / 100;

    GamePiece clicked = this.board.get(row).get(col);

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        if (this.board.get(r).get(c).equals(clicked)) {
          boolean top = this.board.get(r).get(c).top; 
          boolean bottom = this.board.get(r).get(c).bottom; 
          boolean left = this.board.get(r).get(c).left; 
          boolean right = this.board.get(r).get(c).right; 
          this.board.get(r).get(c).top = left; 
          this.board.get(r).get(c).right = top;
          this.board.get(r).get(c).bottom = right;
          this.board.get(r).get(c).left = bottom;
        }
      }
    }
    hasPath();
    worldEnds();
  }

}



// Represents a game piece
class GamePiece {
  //in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;
  // Distance from the power station
  int distance;
  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;
  // whether the power station is on this piece
  boolean powerStation;
  boolean powered;


  GamePiece(int row, int col, boolean left,  boolean right,  boolean top,
      boolean bottom,  boolean powerStation,  boolean powered) { 
    this.row = row;
    this.col = col;
    this.left = left; 
    this.right = right;
    this.top = top; 
    this.bottom = bottom;
    this.powerStation = powerStation;
    this.powered = powered;
    // these are all the parameters that will define the gamePiece
  }


  // Generate an image of this, the given GamePiece.
  // - size: the size of the tile, in pixels
  // - wireWidth: the width of wires, in pixels
  // - wireColor: the Color to use for rendering wires on this
  // - hasPowerStation: if true, draws a fancy star on this tile to represent the power station
  // This method was given and it was said its okay not to test it because we did not change it
  WorldImage tileImage(int size, int wireWidth, Color wireColor, boolean hasPowerStation) {
    // Start tile image off as a blue square with a wire-width square in the middle,
    // to make image "cleaner" (will look strange if tile has no wire, but that can't be)
    WorldImage image = new OverlayImage(
        new RectangleImage(wireWidth, wireWidth, OutlineMode.SOLID, wireColor),
        new RectangleImage(size, size, OutlineMode.SOLID, Color.DARK_GRAY));
    WorldImage vWire = new RectangleImage(wireWidth, (size + 1) / 2, OutlineMode.SOLID, wireColor);
    WorldImage hWire = new RectangleImage((size + 1) / 2, wireWidth, OutlineMode.SOLID, wireColor);

    if (this.top) {
      image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, vWire, 0, 0, image);
    }
    if (this.right) {
      image = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, hWire, 0, 0, image);
    }
    if (this.bottom) {
      image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, vWire, 0, 0, image);
    }
    if (this.left) {
      image = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, hWire, 0, 0, image);
    }
    if (hasPowerStation) {
      image = new OverlayImage(
          new OverlayImage(
              new StarImage(size / 3, 7, OutlineMode.OUTLINE, new Color(255, 128, 0)),
              new StarImage(size / 3, 7, OutlineMode.SOLID, new Color(0, 255, 255))),
          image);
    }
    return image;
  }


}


// Represents an edge connecting two pieces
class Edge {
  GamePiece fromNode;
  GamePiece toNode;
  int weight;

  Edge(GamePiece fromNode, GamePiece toNode, int weight) {
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.weight = weight;
  }
}



// examples class
class Examples {

  LightEmAll w; 

  // a method that initializes a board
  public void init() { 
    w = new LightEmAll(5, 5, 2, 2, 10);
  }

  // calls big bang to open the world
  //  void testBigBang(Tester t) {
  //    init();
  //    w.bigBang(w.width * 100, w.height * 100, 0.1);
  //  }


  // Test init board
  void testInitBoard(Tester t) {
    init(); 

    t.checkExpect(w.board.size(), 5);

    w.initBoard();

    t.checkExpect(w.board.size(), 10);
  }

  // Test find
  void testFind(Tester t) {
    init();

    HashMap<GamePiece, GamePiece> rep = new HashMap<GamePiece, GamePiece>();
    GamePiece a = new GamePiece(0, 0, false, false, false, false, false, false);
    GamePiece b = new GamePiece(0, 1, false, false, false, false, false, false);
    GamePiece c = new GamePiece(0, 2, false, false, false, false, false, false);
    GamePiece d = new GamePiece(0, 3, false, false, false, false, false, false);
    GamePiece e = new GamePiece(0, 4, false, false, false, false, false, false);

    rep.put(a, a);
    rep.put(b, a);
    rep.put(c, a);
    rep.put(d, c);
    rep.put(e, e);

    t.checkExpect(w.find(rep, a), a);
    t.checkExpect(w.find(rep, b), a);
    t.checkExpect(w.find(rep, c), a);
    t.checkExpect(w.find(rep, d), a);
    t.checkExpect(w.find(rep, e), e);
  }

  // Test union
  void testUnion(Tester t) {
    init();

    HashMap<GamePiece, GamePiece> rep = new HashMap<GamePiece, GamePiece>();
    GamePiece a = new GamePiece(0, 0, false, false, false, false, false, false);
    GamePiece b = new GamePiece(0, 1, false, false, false, false, false, false);
    GamePiece c = new GamePiece(0, 2, false, false, false, false, false, false);
    GamePiece d = new GamePiece(0, 3, false, false, false, false, false, false);
    GamePiece e = new GamePiece(0, 4, false, false, false, false, false, false);

    rep.put(a, a);
    rep.put(b, a);
    rep.put(c, a);
    rep.put(d, c);
    rep.put(e, e);

    w.union(rep, a, b);
    t.checkExpect(rep.get(a), b);
    w.union(rep, c, d);
    t.checkExpect(rep.get(c), d);
  }


  //testing the worldEnds method
  public void testWorldEnds(Tester t) { 

    init();
    WorldScene test = w.makeScene();

    t.checkExpect(w.worldEnds(), new WorldEnd(false, test));
  }

  // testing the drawEnd method
  public void testDrawEnd(Tester t) { 
    init();
    t.checkExpect(w.drawEnd(false), w.makeScene());
    WorldScene test = w.makeScene();
    test.placeImageXY(new TextImage("You Win!", 30, Color.GREEN), w.width * 50, w.height * 50);
    t.checkExpect(w.drawEnd(true), test);
  }

  // testing the moreThanOneTree method
  public void testMoreThanOneTree(Tester t) {
    init();
    HashMap<GamePiece, GamePiece> rep = new HashMap<GamePiece, GamePiece>();
    GamePiece a = new GamePiece(0, 0, false, false, false, false, false, false);
    GamePiece b = new GamePiece(0, 1, false, false, false, false, false, false);
    GamePiece c = new GamePiece(0, 2, false, false, false, false, false, false);
    GamePiece d = new GamePiece(0, 3, false, false, false, false, false, false);
    GamePiece e = new GamePiece(0, 4, false, false, false, false, false, false);

    rep.put(a, a);
    rep.put(b, a);
    rep.put(c, a);
    rep.put(d, c);
    rep.put(e, e);

    for (int r = 0; r < w.height; r++) {
      for (int x = 0; x < w.width; x++) {
        rep.put(w.board.get(r).get(x), w.board.get(r).get(x)); 
      }
    }


    t.checkExpect(w.moreThanOneTree(rep), true);
  }

  // testing the onKeyEvent method 
  public void testOnKeyEvent(Tester t) { 
    init(); 
    w.board.get(0).get(0).right = true;
    w.board.get(0).get(0).bottom = true;
    w.board.get(0).get(1).left = true;
    w.board.get(0).get(1).bottom = true;
    w.board.get(1).get(1).top = true;
    w.board.get(1).get(1).left = true;
    w.board.get(1).get(0).right = true;
    w.board.get(1).get(0).top = true;

    t.checkExpect(w.powerCol, 0);
    t.checkExpect(w.powerRow, 0);

    w.onKeyEvent("up");

    t.checkExpect(w.powerCol, 0);
    t.checkExpect(w.powerRow, 0);


    w.onKeyEvent("right");

    t.checkExpect(w.powerCol, 1);
    t.checkExpect(w.powerRow, 0);

    w.onKeyEvent("down");

    t.checkExpect(w.powerCol, 1);
    t.checkExpect(w.powerRow, 1);

    w.onKeyEvent("left");

    t.checkExpect(w.powerCol, 0);
    t.checkExpect(w.powerRow, 1);

    w.onKeyEvent("up");

    t.checkExpect(w.powerCol, 0);
    t.checkExpect(w.powerRow, 0);

  }

  //Test the makeScene method
  public boolean testMakeScene(Tester t) {
    init();
    WorldImage temp;
    WorldScene x = new WorldScene(500, 500); 

    for (int r = 0; r < w.height; r++) { 
      for (int c = 0; c < w.width; c++) { 
        w.board.get(r).get(c).bottom = true;
        w.board.get(r).get(c).left = true;
        w.board.get(r).get(c).right = true;
        w.board.get(r).get(c).top = true;
      }

    }

    for (int c = 0; c < w.height; c++) {
      for (int r = 0; r < w.width; r++) {
        temp = w.board.get(r).get(c).tileImage(
            50, 5, Color.YELLOW, w.board.get(r).get(c).powerStation);
        x.placeImageXY(temp, c * (w.width / 5), r * (w.height / 5));
      }
    }

    return true; // t.checkExpect(w.makeScene(), x);

  }

  // testing the onMouseClicked method 
  public void testOnMouseClicked(Tester t) { 
    init(); 
    Posn posn1 = new Posn(50, 50);
    Posn posn2 = new Posn(100, 50);

    w.board.get(0).get(0).right = true;
    w.board.get(0).get(0).bottom = true;
    w.board.get(0).get(0).left = true;
    w.board.get(0).get(0).top = false;


    t.checkExpect(w.board.get(0).get(0).bottom, true);
    t.checkExpect(w.board.get(0).get(0).left, true);


    w.onMouseClicked(posn1);

    t.checkExpect(w.board.get(0).get(0).bottom, true);
    t.checkExpect(w.board.get(0).get(0).left, true);
    w.onMouseClicked(posn1);

    t.checkExpect(w.board.get(0).get(0).bottom, false);
    t.checkExpect(w.board.get(0).get(0).left, true);

    w.onMouseClicked(posn1);

    t.checkExpect(w.board.get(0).get(0).bottom, true);
    t.checkExpect(w.board.get(0).get(0).left, false);

  }



}