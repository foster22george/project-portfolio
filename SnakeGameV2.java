
import java.util.ArrayList;

import tester.*;
//import javalib.funworld.World;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;


// the given code for the object LightEmAll that extends the World class
class SnakeWorld extends World {
  // a list of columns of Cells,
  // i.e., represents the board in column-major order
  ArrayList<ArrayList<Cell>> board;
  // a list of all nodes
  ArrayList<Cell> snake;

  // the width and height of the board
  int width;
  int height;
  Color snakeColor;
  String snakeDir;



  SnakeWorld(int width, int height, Color snakeColor, String snakeDir) { 
    this.width = width; 
    this.height = height;
    this.snakeColor = snakeColor;
    this.snakeDir = snakeDir;

    this.board = new ArrayList<ArrayList<Cell>>();
    this.snake = new ArrayList<Cell>();

    this.initBoard();
  }


  // EFFECT: initializes the board
  // Creates a board with one possible solution and each tile randomly rotated
  public void initBoard() { 
   

    // right here i try and just create a board with all unused cells (grass)
    for (int r = 0; r < this.height; r++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      for (int c = 0; c < this.width; c++) { 
        row.add(new Cell(r, c, 'U'));
      }
      this.board.add(row);
    }

    // creating the snake
    this.board.get(this.height / 2).get(3).type = 'H';
    this.board.get(this.height / 2).get(2).type = 'S';
    this.board.get(this.height / 2).get(1).type = 'T';
    snake.add(0, this.board.get(this.height / 2).get(1));
    snake.add(0, this.board.get(this.height / 2).get(2));
    snake.add(0, this.board.get(this.height / 2).get(3));
    
    // adding the apple 
    this.board.get(this.height / 2).get(5).type = 'A';
    
    worldEnds();
  }

 
  // Overiring the worldEnd method to see if the game should be ended. Either returns a final 
  // scene or contines on 
  public WorldEnd worldEnds() {
    if (this.gameOver()) {
      return new WorldEnd(true, this.drawEnd(true));
    }
    return new WorldEnd(false, this.drawEnd(false));
  }
  
  
  // checks to see if the game is over
  public boolean gameOver() { 
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        if(this.board.get(i).get(j).type == 'H') { 

          // if the snake is facing up: check if there is a wall or part of the snake
          if(snakeDir.equals("top")) { 
            if(i == 0 || this.board.get(i - 1).get(j).type == 'S'  || this.board.get(i - 1).get(j).type == 'T') { 
              return true;
            }   
          }

          // if the snake is facing down: check if there is a wall or part of the snake
          else if(snakeDir.equals("bottom")) { 
            if(i == this.height - 1 || this.board.get(i + 1).get(j).type == 'S' || this.board.get(i + 1).get(j).type == 'T') { 
              return true;
            }
          }

          // if the snake is facing left: check if there is a wall or part of the snake
          else if(snakeDir.equals("left")) { 
            if(j == 0 || this.board.get(i).get(j - 1).type == 'S' || this.board.get(i).get(j - 1).type == 'T') { 
              return true;
            }
          }

          // if the snake is facing right: check if there is a wall or part of the snake
          else  { 
            if(j == this.width - 1 || this.board.get(i).get(j + 1).type == 'S' || this.board.get(i).get(j + 1).type == 'T') { 
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  public void checkApple() { 
    boolean apple = false;
    // check for an apple
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        if(this.board.get(i).get(j).type == 'A') { 
          apple = true;  
        }
      }
    }

    // if not apple add one to snakes body 
    this.moveHead();
    this.moveTail(apple);
    
    // if there is already an apple then don't add an apple
    if(apple) { 
      return;
    }

    // because its random I do more than just the amount of tiles
    for(int i = 0; i < height * width * 10; i++) { 
      int randRow =  new Random().nextInt(this.height);
      int randCol =  new Random().nextInt(this.width);
      if(this.board.get(randRow).get(randCol).type == 'U') { 
        this.board.get(randRow).get(randCol).type = 'A';
        return;
      }
    }
  }

  // if there is an apple eaten then the tail doesn't move
  public void moveTail(boolean keep) { 

    if(keep) {
      Cell temp  = new Cell(0, 0, 'U'); 
      temp = snake.remove(snake.size() - 1);
      temp.type = 'U';
      snake.get(snake.size() - 1).type = 'T';
    }
  }

  //if there is an apple eaten then the tail doesn't move
  public void moveHead(){ 
    // finds the head
    int j = this.snake.get(0).col;
    int i = this.snake.get(0).row;
     
    this.board.get(i).get(j).type = 'S';
    if(snakeDir.equals("top")) { 
      this.board.get(i - 1).get(j).type = 'H';
      snake.add(0, this.board.get(i - 1).get(j));
    }

    else if(snakeDir.equals("bottom")) { 
      this.board.get(i + 1).get(j).type = 'H';
      snake.add(0, this.board.get(i + 1).get(j));
    }

    else if(snakeDir.equals("left")) { 
      this.board.get(i).get(j - 1).type = 'H';
      snake.add(0, this.board.get(i).get(j - 1));
    }

    else if(snakeDir.equals("right")) { 
      this.board.get(i).get(j + 1).type = 'H';
      snake.add(0, this.board.get(i).get(j + 1));
    }
  }



  // this returns a worldScene that is drawn if the game is won
  public WorldScene drawEnd(boolean end) {
    WorldScene w = this.makeScene();
    if (end) {
      w.placeImageXY(new TextImage("You Lose!", 30, Color.RED), this.width * 25,
          this.height * 25);
    }
    return w;
  }



  // returns the worldScene that is drawn
  public WorldScene makeScene() {

    WorldScene w = new WorldScene(50 * this.width, 50 * this.height);  
    WorldImage temp;
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        temp = board.get(r).get(c).cellImage(50 , this.snakeColor, this.snakeDir);
        w.placeImageXY(temp, c * 50 + 25, r * 50 + 25);
      }
    }
    return w;
  }



  // changes the direction of the snake
  public void onKeyEvent(String k) {
    if (k.equals("up")) {
      this.snakeDir = "top";
      //return this;
    }
    else if (k.equals("down")) {
      this.snakeDir = "bottom";
      
    }
    else if (k.equals("left")) {
      this.snakeDir = "left";
      
    }
    else if (k.equals("right")) {
      this.snakeDir = "right";
      
    }
    worldEnds();
  }
  
  // Overides the onTick method
  public void onTick() {
    //this.worldEnds();
    this.makeScene();
    this.checkApple();
    //return new SnakeWorld(this.height, this.width, this.snakeColor, this.snakeDir);
  }
}



// Represents a game piece
class Cell {
  //in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;
  
  //what type of cell it is
  char type;
  


  Cell(int row, int col, char type) { 
    this.row = row;
    this.col = col;
    this.type = type;
  }


  // Generate an image of this, the given Cell.
  // - size: the size of the tile, in pixels
  // - snakeColor: the Color to use for drawing the snake 
  // - snakeDir: to know which way the snake should be facing (applies to head and tail
  WorldImage cellImage(int size, Color snakeColor, String snakeDir) {
    // Start tile image off either as a square of color green (for grass)
    WorldImage image = new RectangleImage(size, size, OutlineMode.SOLID, Color.GREEN);

    // changes the color if the cell is apart of the snake
    if(this.type == 'S' || this.type == 'H' || this.type == 'T') { 
       image = new RectangleImage(size, size, OutlineMode.SOLID, snakeColor);
    }
    
    
    WorldImage tail = new RectangleImage((size + 1) / 2, size / 5, OutlineMode.SOLID, Color.LIGHT_GRAY);
    WorldImage eye = new RectangleImage((size / 10), (size / 10), OutlineMode.SOLID, Color.WHITE);
    WorldImage apple = new CircleImage(((size / 2) - (size / 10)), OutlineMode.SOLID, Color.RED);

    // adds the top layer for the tail and the head
    if (this.type == 'T') {
       image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, tail, 0, 0, image);
    }
    else if (this.type == 'H') {
       image = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, eye, 0, 0, image);
       image = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, eye, 0, 0, image);
    }
    else if (this.type == 'A') {
      image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.MIDDLE, apple, 0, 0, image);
   }
    
    
    // no need to rotate the image because its already facing the top
    
    // rotate to the right
    if(snakeDir.equals("right")) { 
       image = new RotateImage(image, 90);
    }
    // rotate to the bottom
    else if(snakeDir.equals("bottom")) { 
       image = new RotateImage(image, 180);
    }
    // rotate to the bottom
    else if(snakeDir.equals("left")) { 
       image = new RotateImage(image, 270);
    }
       
    return image;
  }
}


// examples class
class ExamplesSnakes {

  SnakeWorld s; 

  // a method that initializes a board
  public void init() { 
    s = new SnakeWorld(20, 20, Color.BLUE, "right");
  }

  // calls big bang to open the world
    void testBigBang(Tester t) {
      init();
      s.bigBang(s.width * 50, s.height * 50, .1);
    }
}
