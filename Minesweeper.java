/* 

  READ ME: For extra credit, we implemented a mine counter 
  which properly displays the amount of mines left according 
  to the flags you have used
  We also implemented the same coloring system for our numbers
  that is used on the google minesweeper game

 */

import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;

// Represents a cell in the minesweeper game
class Cell {
  boolean isMine;
  boolean isRevealed;
  boolean isFlagged;
  int numMines = 0;
  ArrayList<Cell> neighbors;

  Cell(boolean isMine, boolean isRevealed, int numMines) {
    this.isMine = isMine;
    this.isRevealed = isRevealed;
    this.numMines = numMines;
    this.isFlagged = false;
    this.neighbors = new ArrayList<Cell>();
  }

  // Count the number of mines in the neighbors
  void countMines() {
    for (Cell c : this.neighbors) {
      if (c.isMine) {
        this.numMines++;
      }
    }

  }

}

// Represents a full board of cells
class FullBoard {
  ArrayList<ArrayList<Cell>> board;
  int rows;
  int cols;
  int numMines;

  FullBoard(int rows, int cols, int numMines) {
    this.rows = rows;
    this.cols = cols;
    this.numMines = numMines;
    this.board = new ArrayList<ArrayList<Cell>>();


  }


  // Initialize the board with cells and link them together
  void initBoard() {
    for (int i = 0; i < rows; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      for (int j = 0; j < cols; j++) {
        row.add(new Cell(false, false, 0));
        if ((i > 0) && (j > 0)) {
          // Link cell to the left
          row.get(j).neighbors.add(row.get(j - 1));
          row.get(j - 1).neighbors.add(row.get(j));

          // Link cell above
          row.get(j).neighbors.add(this.board.get(i - 1).get(j));
          this.board.get(i - 1).get(j).neighbors.add(row.get(j));

          // Link cell diagonal above and left
          row.get(j).neighbors.add(this.board.get(i - 1).get(j - 1));
          this.board.get(i - 1).get(j - 1).neighbors.add(row.get(j));

          // Link cell diagonal above and right
          if (j < this.cols - 1) {
            row.get(j).neighbors.add(this.board.get(i - 1).get(j + 1));
            this.board.get(i - 1).get(j + 1).neighbors.add(row.get(j));
          }

        } 
        else if ((i > 0) && (j == 0)) {
          // Link cell above
          row.get(j).neighbors.add(this.board.get(i - 1).get(j));
          this.board.get(i - 1).get(j).neighbors.add(row.get(j));

          // Link cell diagonal above and right
          if (j < this.cols - 1) {
            row.get(j).neighbors.add(this.board.get(i - 1).get(j + 1));
            this.board.get(i - 1).get(j + 1).neighbors.add(row.get(j));
          }
        }
        else if ((i == 0 ) && (j > 0)) {
          // Link cell to the left
          row.get(j).neighbors.add(row.get(j - 1));
          row.get(j - 1).neighbors.add(row.get(j));
        }
      }
      this.board.add(row);
    }


  }

  // Place mines on the board
  void placeMines() {
    for (int i = 0; i < this.numMines; i++) {
      int randRow =  new Random().nextInt(this.rows);
      int randCol =  new Random().nextInt(this.cols);
      if (!this.board.get(randRow).get(randCol).isMine) {
        this.board.get(randRow).get(randCol).isMine = true;
      }
      else {
        i--;
      }
    }

    // Count mines for each cell
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        this.board.get(i).get(j).countMines();
      }
    }
  }

  // Place mines on the board for testing
  void placeMinesTester(int x) {
    for (int i = 0; i < this.numMines; i++) {
      int randRow =  new Random(x).nextInt(this.rows);
      int randCol =  new Random(x).nextInt(this.cols);
      if (!this.board.get(randRow).get(randCol).isMine) {
        this.board.get(randRow).get(randCol).isMine = true;
      }
      else {
        i--;
      }
    }
  }

  // Draw the board
  public WorldScene draw(WorldScene world, int fontSize) {
    // Color of the number
    Color col = Color.BLACK;
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        Cell c = this.board.get(i).get(j);

        // Following the color of the google 
        //minesweeper as much as possible
        if (c.numMines == 1) {
          col = Color.BLUE;
        } else if (c.numMines == 2) {
          col = Color.GREEN;
        } else if (c.numMines == 3) {
          col = Color.RED;
        } else if (c.numMines == 4) {
          col = Color.MAGENTA;
        } else if (c.numMines == 5) {
          col = Color.YELLOW;
        } else if (c.numMines == 6) {
          col = Color.CYAN;
        } else if (c.numMines == 7) {
          col = Color.DARK_GRAY;
        }
        else if (c.numMines == 8) {
          col = Color.PINK;
        }

        if (c.isRevealed && c.isMine) {
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.RED), j * 30 + 15,
              i * 30 + 15);
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK),
              j * 30 + 15, i * 30 + 15);
        } 
        else if (c.isRevealed && this.board.get(i).get(j).numMines > 0) {
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.GRAY),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(
              new TextImage(this.board.get(i).get(j).numMines + "", fontSize, col),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK),
              j * 30 + 15, i * 30 + 15);
        } else if (c.isRevealed) {
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.GRAY),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK),
              j * 30 + 15, i * 30 + 15);


          // Flood fill the board
          for (Cell x : c.neighbors) {
            if (!x.isMine && !x.isRevealed) {
              x.isRevealed = true;
            }
          }

        }
        else if (c.isFlagged) {
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(new EquilateralTriangleImage(15, OutlineMode.SOLID, Color.GREEN),
              j * 30 + 15, i * 30 + 15);
        }
        else {
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY),
              j * 30 + 15, i * 30 + 15);
          world.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK),
              j * 30 + 15, i * 30 + 15);
        }
      }
    }
    world.placeImageXY(
        new TextImage("Mines: " + this.numMines, fontSize, Color.RED), rows * 50, cols * 17);
    return world;
  }

  // Check if all cells are revealed
  boolean allRevealed() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        if ((!this.board.get(i).get(j).isRevealed)
            && (!this.board.get(i).get(j).isMine)) {
          return false;
        }
      }
    }
    return true;
  }

  // Check if a mine is revealed
  boolean mineRevealed() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        if (this.board.get(i).get(j).isRevealed && this.board.get(i).get(j).isMine) {
          return true;
        }
      }
    }
    return false;
  }

}


// Represents the minesweeper world
class MinesweeperWorld extends World {

  final int CELL_SIZE = 30;
  final Color BACKGROUND_COLOR = Color.DARK_GRAY;

  int screenHeight;
  int screenWidth;
  int fontSize;
  FullBoard board;

  MinesweeperWorld(int rows, int cols, int numMines) {
    this.board = new FullBoard(rows, cols, numMines);
    this.board.initBoard();
    this.board.placeMines();
    this.screenHeight = rows * CELL_SIZE;
    this.screenWidth = cols * CELL_SIZE;
    this.fontSize = CELL_SIZE / 2;
  }

  // Draw the world
  public WorldScene makeScene() {
    return this.board.draw(new WorldScene(this.screenWidth, this.screenHeight), this.fontSize);
  }

  // Handle mouse clicks
  public void onMouseClicked(Posn pos, String button) {
    int row = pos.y / CELL_SIZE;
    int col = pos.x / CELL_SIZE;
    if (button.equals("LeftButton") && this.board.board.get(row).get(col).isMine 
        && !this.board.board.get(row).get(col).isFlagged) {
      this.board.board.get(row).get(col).isRevealed = true;
    }
    if (button.equals("LeftButton") && !this.board.board.get(row).get(col).isFlagged) {
      this.board.board.get(row).get(col).isRevealed = true;
    } else if (!this.board.board.get(row).get(col).isRevealed && button.equals("RightButton")) {
      this.board.board.get(row).get(col).isFlagged = !this.board.board.get(row).get(col).isFlagged;
      if (this.board.board.get(row).get(col).isFlagged) {
        this.board.numMines = this.board.numMines - 1;
      } else {
        this.board.numMines = this.board.numMines + 1;
      }
    }
    this.board.draw(new WorldScene(this.screenWidth, this.screenHeight), this.fontSize);
  }

  // Game Over screen
  public WorldScene drawEnd(boolean result) {
    WorldScene w = this.makeScene();
    if (result) {
      w.placeImageXY(new TextImage("You Win!", 30, Color.GREEN), this.screenWidth / 2,
          this.screenHeight / 2);
    }
    else {
      w.placeImageXY(new TextImage("You Lose!", 30, Color.RED), this.screenWidth / 2,
          this.screenHeight / 2);
    }
    return w;
  }

  // Overiring the worldEnd method to see if the game should be ended. Either returns a final 
  // scene or contines on 
  public WorldEnd worldEnds() {
    if (this.board.allRevealed()) {
      return new WorldEnd(true, this.drawEnd(true));
    } else if (this.board.mineRevealed()) {
      return new WorldEnd(true, this.drawEnd(false));
    }
    return new WorldEnd(false, this.drawEnd(false));
  }

}



//Examples
class Examples {
  Cell c1;
  Cell c2;
  Cell c3;
  Cell m1;
  Cell m2;
  Cell m3;

  FullBoard board5x5; // = new FullBoard(5, 5, 2);
  FullBoard board10x10; // = new FullBoard(3, 3, 1);
  FullBoard board30x16; // = new FullBoard(1, 1, 1);
  FullBoard board30x16Rev;
  FullBoard board1x2;
  FullBoard board1x2Rev;

  MinesweeperWorld w1;
  MinesweeperWorld w2;
  MinesweeperWorld w3;
  MinesweeperWorld w4;
  MinesweeperWorld w5;


  // Big bang
  void testBigBang(Tester t) {
    init();
    w3.bigBang(w3.screenWidth, w3.screenHeight + (w3.CELL_SIZE * 2), .1);
  }

  // Initialize data
  void init() {
    c1 = new Cell(false, false, 0);
    c2 = new Cell(false, true, 0);
    c3 = new Cell(false, false, 0);
    m1 = new Cell(true, true, 0);
    m2 = new Cell(true, false, 0);
    m3 = new Cell(true, false, 0);

    board5x5 = new FullBoard(5, 5, 10);
    board10x10 = new FullBoard(10, 10, 40);
    board30x16 = new FullBoard(30, 16, 99);
    board1x2 = new FullBoard(1, 2, 1);
    board1x2Rev = new FullBoard(1, 2, 0);



    w1 = new MinesweeperWorld(5, 5, 10);
    w2 = new MinesweeperWorld(10, 10, 40);
    w3 = new MinesweeperWorld(16, 30, 99);
    w4 = new MinesweeperWorld(1, 2, 1);
  }

  // Test the countMines method
  void testCountMines(Tester t) {
    init();
    c1.neighbors.add(m1);
    c1.neighbors.add(m2);
    c1.neighbors.add(m3);
    c1.neighbors.add(c2);
    c1.neighbors.add(c3);
    c2.neighbors.add(c1);
    c2.neighbors.add(c3);
    m1.neighbors.add(c1);
    m1.neighbors.add(m2);

    t.checkExpect(c1.numMines, 0);
    t.checkExpect(c2.numMines, 0);
    t.checkExpect(m1.numMines, 0);

    c1.countMines();
    c2.countMines();
    m1.countMines();

    t.checkExpect(c1.numMines, 3);
    t.checkExpect(c2.numMines, 0);
    t.checkExpect(m1.numMines, 1);



  }

  // Test the initBoard method
  void testInitBoard(Tester t) {
    init();

    t.checkExpect(board5x5.board, new ArrayList<ArrayList<Cell>>());
    t.checkExpect(board10x10.board, new ArrayList<ArrayList<Cell>>());
    t.checkExpect(board30x16.board, new ArrayList<ArrayList<Cell>>());

    board5x5.initBoard();
    board10x10.initBoard();
    board30x16.initBoard();

    t.checkExpect(board5x5.board.size(), 5);
    t.checkExpect(board5x5.board.get(0).size(), 5);
    t.checkExpect(board10x10.board.size(), 10);
    t.checkExpect(board10x10.board.get(0).size(), 10);
    t.checkExpect(board30x16.board.size(), 30);
    t.checkExpect(board30x16.board.get(0).size(), 16); 

  }

  // Test the placeMines method
  void testPlaceMinesTester(Tester t) {
    init();

    board1x2.initBoard();

    t.checkExpect(board1x2.board.get(0).get(0).isMine, false);
    t.checkExpect(board1x2.board.get(0).get(1).isMine, false);

    board1x2.placeMinesTester(1);

    t.checkExpect(board1x2.board.get(0).get(0).isMine, false);
    t.checkExpect(board1x2.board.get(0).get(1).isMine, true);

  }

  // Test the draw method
  boolean testDraw(Tester t) {
    init();

    WorldScene w = new WorldScene(150, 150);
    w.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY), 15, 15);
    w.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 15, 15);
    w.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY), 45, 15);
    w.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 45, 15);
    w.placeImageXY(new TextImage("Mines: 1", 15, Color.red), 50, 34);// added this line - george


    WorldScene w1 = new WorldScene(150, 150);
    w1.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.GRAY), 15, 15);
    w1.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 15, 15);
    w1.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.GRAY), 45, 15);
    w1.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 45, 15);
    w1.placeImageXY(new TextImage("Mines: 0", 15, Color.red), 50, 34);// added this line - george


    board1x2.initBoard();
    board1x2.placeMines();
    board1x2Rev.initBoard();
    board1x2Rev.placeMines();

    board1x2Rev.board.get(0).get(0).isRevealed = true;
    board1x2Rev.board.get(0).get(1).isRevealed = true;

    return t.checkExpect(board1x2.draw(new WorldScene(150, 150), 15), w)
        &&  t.checkExpect(board1x2Rev.draw(new WorldScene(150, 150), 15), w1);   

  }

  // Test the makeScene method
  boolean testMakeScene(Tester t) {
    init();

    WorldScene w = new WorldScene(60, 30);
    w.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY), 15, 15);
    w.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 15, 15);
    w.placeImageXY( new RectangleImage(30, 30, OutlineMode.SOLID, Color.DARK_GRAY), 45, 15);
    w.placeImageXY(new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.BLACK), 45, 15);
    w.placeImageXY(new TextImage("Mines: 1", 15, Color.red), 50, 34); // added this line - george

    return t.checkExpect(w4.makeScene(), w);

  }

  // tests the allRevelead method
  void testAllRevelead(Tester t) { 
    init();
    board5x5.initBoard();
    board10x10.initBoard();
    t.checkExpect(board5x5.allRevealed(), false);
    t.checkExpect(board10x10.allRevealed(), false);

    // now revealing all the cells and mutating my data
    for (int i = 0; i < 5; i++) { 
      for (int j = 0; j < 5; j++) { 
        this.board5x5.board.get(i).get(j).isRevealed = true;
      }
    }

    t.checkExpect(board5x5.allRevealed(), true);

    // mutating one cell in the whole board
    this.board10x10.board.get(3).get(7).isRevealed = true;

    //checking if there the method works if a cell reveleaed
    t.checkExpect(board10x10.allRevealed(), false);

    // fully revealing all the cells
    for (int i = 0; i < 10; i++) { 
      for (int j = 0; j < 10; j++) { 
        this.board10x10.board.get(i).get(j).isRevealed = true;
      }
    }

    // make sure the method works on a larger board
    t.checkExpect(board10x10.allRevealed(), true);

  }


  // testing the mineRevealed method
  void testMineRevealed(Tester t) { 

    init();
    board1x2.initBoard();
    board1x2.placeMinesTester(1);

    t.checkExpect(board1x2.mineRevealed(), false); // has a mine (not revealed)

    board1x2.board.get(0).get(0).isRevealed = true; // reveal a non mine
    board1x2.board.get(0).get(1).isRevealed = true; // reveal a mine

    t.checkExpect(board1x2.mineRevealed(), true); // has a mine (revealed)

  }

  // testing the drawEnd method 
  boolean testDrawEnd(Tester t) { 
    init();
    board1x2.initBoard();

    // WorldScene w1 = new WorldScene(60, 30);
    WorldScene test = w4.makeScene();
    test.placeImageXY(new TextImage("You Lose!", 30, Color.RED), 30,  15); 
    WorldScene test2 = w1.makeScene();
    test2.placeImageXY(new TextImage("You Win!", 30, Color.GREEN), 75,  75);

    return t.checkExpect(w4.drawEnd(false), test)
        && t.checkExpect(w1.drawEnd(true), test2);
  }


  // testing the onMouseClicked method
  void testOnMouseClicked(Tester t) {
    init();
    w4.board.initBoard();

    t.checkExpect(w4.board.board.get(0).get(0).isRevealed, false);

    // testing the left button
    w4.onMouseClicked(new Posn(20, -10), "LeftButton");
    t.checkExpect(w4.board.board.get(0).get(0).isRevealed, true);

    init();
    w4.board.initBoard();

    t.checkExpect(w4.board.board.get(0).get(1).isFlagged, false);

    // testing the right button
    w4.onMouseClicked(new Posn(40, -10), "RightButton");
    t.checkExpect(w4.board.board.get(0).get(1).isFlagged, true);
    t.checkExpect(w4.board.numMines, 0);

    // testing the right button again
    w4.onMouseClicked(new Posn(40, -10), "RightButton");
    t.checkExpect(w4.board.board.get(0).get(1).isFlagged, false);
    t.checkExpect(board1x2.numMines, 1);

  }
  
  //testing the worldEnds method
  void testWorldEnds(Tester t) { 
    
    init();
    board1x2.initBoard();

    board1x2.board.get(0).get(0).isRevealed = true; // reveal a non mine
    board1x2.board.get(0).get(1).isRevealed = true; // reveal a mine

    WorldScene test = w4.makeScene();

    test.placeImageXY(new TextImage("You Lose!", 30, Color.RED), 30,  15); 

    t.checkExpect(w4.worldEnds(), new WorldEnd(false, test));


    // we tried testing the other result here when the worldEnd() method gets 
    // called but the case is false so method tries to execute this code: 
    // return new WorldEnd(false, this.drawEnd(false)); but for some reason 
    // this can't be tested because we get a failure saying <irrelevant> and 
    // so we posted on Piazza, but Professor Razzaq said to not worry about that case. 


  }

  /* 

 READ ME: For extra credit, we implemented a mine counter 
 which properly displays the amount of mines left according 
 to the flags you have used
 We also implemented the same coloring system for our numbers
 that is used on the google minesweeper game

   */
}
