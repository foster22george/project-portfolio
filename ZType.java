import tester.Tester;
import java.awt.Color;
import java.util.Random;

import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;




//represents a list of words
interface ILoWord {

  // Takes a String of length one and then removes the first occurrence from all words
  // starting with the letter when there isn't an active word
  ILoWord checkAndReduceInactive(String str);

  // Takes a String of length one and then removes the first occurrence from all words
  // starting with the letter when there is a active word
  ILoWord checkAndReduceActive(String str);

  // Returns the list with all empty strings removed
  ILoWord filterOutEmpties();

  // Draws all words onto the worldscene
  WorldScene draw(WorldScene world);

  //return a new list of words with the same words at a lower y value
  ILoWord move();

  // return true if the list of words contains an active word
  boolean hasActiveWord();

  // return true if a words y coordinate reaches the bottom of the screen
  boolean gameOver(int bottom);

}

//represents an empty list of words
class MtLoWord implements ILoWord {


  /*
   * Template
   * Fields:
   * Methods:
   * this.checkAndReduceInactive(String) ... ILoWord
   * this.filterOutEmpties() ... ILoWord
   * this.draw(WorldScene) ... WorldScene
   * this.move() ... ILoWord
   * Methods for fields:
   * none
   */




  // Takes a String of length one and then removes the first occurrence from all words
  // starting with the letter
  public ILoWord checkAndReduceInactive(String str) {
    return this;
  }

  // reduces the first letter of the word if str matches
  public ILoWord checkAndReduceActive(String str) {
    return this;
  }


  // Returns the list with all empty strings removed
  public ILoWord filterOutEmpties() {
    return this;
  }

  // Draws all words onto the worldscene
  public WorldScene draw(WorldScene world) {
    return world;
  }

  //return a new list of words with the same words at a lower y value
  public ILoWord move() {
    return this;
  }

  // return true if the list of words contains an active word
  public boolean hasActiveWord() {
    return false;
  }

  // return true if a words y coordinate reaches the bottom of the screen
  public boolean gameOver(int bottom) {
    return false;
  }
}

//represents a non-empty list of words
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * Template
   * Fields:
   * this.first ... IWord
   * this.rest ... ILoWord
   * Methods:
   * this.checkAndReduceInactive(String) ... ILoWord
   * this.filterOutEmpties() ... ILoWord
   * this.draw(WorldScene) ... WorldScene
   * this.move() ... ILoWord
   * Methods for fields:
   * this.first.lowerCaseWord() ... String
   * this.first.toImage(WorldScene) ... WorldScene
   * this.first.removeFirst() ... IWord
   * this.first.drawWord() ... WorldImage
   * this.first.moveHelper() ... IWord
   * this.first.isActive() ... boolean
   * this.first.findWord() ... int
   * this.rest.checkAndReduceInactive(String) ... ILoWord
   * this.rest.filterOutEmpties() ... ILoWord
   * this.rest.draw(WorldScene) ... WorldScene
   * this.rest.move() ... ILoWord
   * this.rest.hasActiveWord() ... boolean
   * this.rest.gameOver(int) ... boolean
   */


  // Takes a String of length one and then removes the first letter if it matches the string
  public ILoWord checkAndReduceInactive(String str) {
    if (str.length() != 1) {
      return this;
    }

    if (this.first.lowerCaseWord().length() > 0 
        && this.first.lowerCaseWord().substring(0,1).equals(str)) {
      return new ConsLoWord(this.first.removeFirst(), this.rest);
    } else {
      return new ConsLoWord(first, this.rest.checkAndReduceInactive(str));
    }
  }

  // Takes a String of length one and then removes the first letter if it matches the string
  public ILoWord checkAndReduceActive(String str) {
    if (str.length() != 1) {
      return this;
    }

    if (this.first.lowerCaseWord().length() > 0
        && this.first.lowerCaseWord().substring(0, 1).equals(str)
        && this.first.isActive()) {
      return new ConsLoWord(this.first.removeFirst(), this.rest);
    } else {
      return new ConsLoWord(this.first, this.rest.checkAndReduceActive(str));
    }
  }


  // Returns the list with all empty strings removed
  public ILoWord filterOutEmpties() {
    if (this.first.lowerCaseWord().equals("")) {
      return this.rest.filterOutEmpties();
    } else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // Draws all words onto the worldscene
  public WorldScene draw(WorldScene world) {
    return this.first.toImage(this.rest.draw(world));
  }

  // return a new list of words with the same words at a lower y value
  public ILoWord move() {
    return new ConsLoWord(this.first.moveHelper(), this.rest.move());
  }

  // return true if the list of words contains an active word
  public boolean hasActiveWord() {
    if (this.first.isActive()) {
      return true;
    }
    else {
      return this.rest.hasActiveWord();
    }
  }

  // return true if a words y coordinate reaches the bottom of the screen
  public boolean gameOver(int bottom) {
    if (this.first.findWord() >= bottom) {
      return true;
    }
    else {
      return this.rest.gameOver(bottom);
    }
  }
}

//represents a word in the ZType game
interface IWord {

  // Constant
  final int FONT_SIZE = 20;

  // Returns the lower case version of the word
  String lowerCaseWord();

  // Draws an image of a word
  WorldScene toImage(WorldScene world);

  // Removes the first letter of a word
  IWord removeFirst();

  //draws the word onto the worldScene
  WorldImage drawWord();

  //return a new word with the same word at a lower y value
  IWord moveHelper();

  //return true if the word is active
  boolean isActive();

  // Return where the word is
  int findWord();
}


//represents an active word in the ZType game
class ActiveWord implements IWord {
  String word;
  int x;
  int y;
  Color c;

  ActiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
    this.c = Color.RED;
  }

  /*
   * Template
   * Fields:
   * this.word ... String
   * this.x ... int
   * this.y ... int
   * this.c ... Color
   * methods:
   * this.lowerCaseWord() ...           - String
   * this.toImage(WorldScene) ...       - WorldScene
   * this.removeFirst()...              - IWord
   * this.drawWord()...                 - WorldImage
   * this.moveHelper()...                     - IWord
   * methods of fields:
   * none
   */

  // Draws the word onto the worldScene
  public WorldImage drawWord() {
    return new TextImage(this.word, FONT_SIZE, this.c);
  }

  // Returns the lower case version of the word
  public String lowerCaseWord() {
    return this.word.toLowerCase();
  }

  // Draws an image of a word
  public WorldScene toImage(WorldScene world) {
    return world.placeImageXY(new TextImage(this.word, FONT_SIZE, this.c), this.x, this.y);
  }

  // Removes the first letter of a word
  public IWord removeFirst() {
    return new ActiveWord(this.lowerCaseWord().substring(1), this.x, this.y);
  }

  //return a new word with the same word at a lower y value
  public IWord moveHelper() {
    return new ActiveWord(this.word, this.x, this.y + 5);
  }

  //return true if the word is active
  public boolean isActive() {
    return true;
  }

  // Return where the word is
  public int findWord() {
    return this.y;
  }

}

//represents an inactive word in the ZType game
class InactiveWord implements IWord {
  String word;
  int x;
  int y;
  Color c;

  InactiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
    this.c = Color.BLUE;
  }

  /*
   * Template
   * Fields:
   * this.word ... String
   * this.x ... int
   * this.y ... int
   * this.c ... Color
   * methods:
   * this.lowerCaseWord() ...           - String
   * this.toImage(WorldScene) ...       - WorldScene
   * this.removeFirst()...              - IWord
   * this.drawWord()...                 - WorldImage
   * this.moveHelper()...                     - IWord
   * methods of fields:
   * none
   */


  // Draws the word onto the worldScene
  public WorldImage drawWord() {
    return new TextImage(this.word, FONT_SIZE, this.c); 
  }

  // Returns the lower case version of the word
  public String lowerCaseWord() {
    return this.word.toLowerCase();
  }

  // Draws an image of a word
  public WorldScene toImage(WorldScene world) {
    return world.placeImageXY(new TextImage(this.word, FONT_SIZE, this.c), this.x, this.y);
  }

  // Removes the first letter of a word
  public IWord removeFirst() {
    return new ActiveWord(this.lowerCaseWord().substring(1), this.x, this.y);
  }

  //return a new word with the same word at a lower y value
  public IWord moveHelper() {
    return new InactiveWord(this.word, this.x, this.y + 5);
  }

  //return true if the word is active
  public boolean isActive() {
    return false;
  }

  // Return where the word is
  public int findWord() {
    return this.y;
  }

}

//represents a world in the ZType game
class ZTypeWorld extends World {

  // constants
  final int SCREEN_HEIGHT = 500;
  final int SCREEN_WIDTH = 500;
  final Color BACKGROUND_COLOR = Color.WHITE;
  final int WORD_GENERATION_RATE = 100;

  // fields
  int screenHeight;
  int screenWidth;
  int fontSize;
  Color backgroundColor;
  ILoWord words;
  int tickCount;

  /*
   * Template
   * Fields:
   * this.screenHeight ... int
   * this.screenWidth ... int
   * this.fontSize ... int
   * this.backgroundColor ... Color
   * this.words ... ILoWord
   * this.tickCount ... int
   * Methods:
   * this.makeScene() ... WorldScene
   * this.onTick() ... World
   * this.onKeyEvent(String) ... ZTypeWorld
   * Methods for fields:
   * this.words.draw(WorldScene) ... WorldScene
   * this.words.checkAndReduceInactive(String) ... ILoWord
   * this.words.checkAndReduceActive(String) ... ILoWord
   * this.words.filterOutEmpties() ... ILoWord
   * this.words.move() ... ILoWord
   * this.words.hasActiveWord() ... boolean
   * this.words.gameOver(int) ... boolean
   */

  ZTypeWorld(ILoWord word) {
    this.words = word;
    this.screenHeight = SCREEN_HEIGHT;
    this.screenWidth = SCREEN_WIDTH;
    this.backgroundColor = BACKGROUND_COLOR;
    this.tickCount = 0;
  }


  // the to-draw method for big bang essentially 
  public WorldScene makeScene() {
    return this.words.draw(new WorldScene(this.SCREEN_WIDTH, this.SCREEN_HEIGHT));
  }

  // Overides the onTick method
  public World onTick() {
    this.makeScene();
    this.tickCount++;
    if (this.tickCount % WORD_GENERATION_RATE == 1) {
      this.words = new ConsLoWord(new InactiveWord(
          new Utils().produceString() , 
          new Random().nextInt(SCREEN_WIDTH - 50) + 25, 0), this.words);
    }
    this.words = this.words.filterOutEmpties();


    if (words.gameOver(SCREEN_HEIGHT - 10)) {
      return this.endOfWorld("Game Over");
    }
    return new ZTypeWorld(this.words.move());    

  }

  // Overides the onKeyEvent method
  public ZTypeWorld onKeyEvent(String key) {
    if (words.hasActiveWord()) {
      return new ZTypeWorld(this.words.checkAndReduceActive(key));
    } else {
      return new ZTypeWorld(this.words.checkAndReduceInactive(key));
    }
  }



  // Creates an end of game scene
  public WorldScene lastScene(String str) {
    return new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT)
        .placeImageXY(
            new TextImage(str , 20, FontStyle.REGULAR, Color.RED),
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
  }
}


//represents a utility class
class Utils {

  /*
   * Template
   * Fields:
   * Methods:
   * this.produceString() ... String
   * this.produceStringTester() ... String
   * Methods for fields:
   * none
   */

  // produces a string with six random characters in it
  String produceString() {
    return Character.toString(97 + (new Random().nextInt(26))) 
        + Character.toString(97 + (new Random().nextInt(26)))
        + Character.toString(97 + (new Random().nextInt(26))) 
        + Character.toString(97 + (new Random().nextInt(26))) 
        + Character.toString(97 + (new Random().nextInt(26)))
        + Character.toString(97 + (new Random().nextInt(26)));
  }

  // produces a string with six random characters in it
  String produceStringTester() {
    return Character.toString(97 + (new Random(1).nextInt(26))) 
        + Character.toString(97 + (new Random(1).nextInt(26)))
        + Character.toString(97 + (new Random(1).nextInt(26))) 
        + Character.toString(97 + (new Random(1).nextInt(26))) 
        + Character.toString(97 + (new Random(1).nextInt(26)))
        + Character.toString(97 + (new Random(1).nextInt(26)));
  }

}


//all examples and tests for ILoWord
class ExamplesWordLists {
  IWord word1 = new InactiveWord("hello", 0, 0);
  IWord word2 = new InactiveWord("west-world", 0, 0);
  IWord word3 = new InactiveWord("", 0, 10);
  IWord word4 = new InactiveWord("FLUFF", 0, 0);
  IWord word5 = new InactiveWord("two", 0, 0);
  IWord word6 = new ActiveWord("est-world", 0, 0);

  ILoWord list1 = new MtLoWord();
  ILoWord list2 = new ConsLoWord(this.word3, this.list1);
  ILoWord list3 = new ConsLoWord(this.word2, this.list1);
  ILoWord list4 = new ConsLoWord(this.word1, this.list3);
  ILoWord list5 = new ConsLoWord(this.word3, this.list4);
  ILoWord list6 = new ConsLoWord(this.word5, this.list5);
  ILoWord list7 = new ConsLoWord(this.word1,
      new ConsLoWord(this.word2,
          new ConsLoWord(this.word2, list1)));
  ILoWord list10 = new ConsLoWord(this.word6, list6);

  Utils u1 = new Utils();

  WorldScene scene1 = new WorldScene(500,500);
  WorldScene scene2 = new WorldScene(200, 75);
  WorldScene scene3 = new WorldScene(300, 600);

  ZTypeWorld ZType1 = new ZTypeWorld(list3);
  ZTypeWorld ZType2 = new ZTypeWorld(list4);
  ZTypeWorld ZType3 = new ZTypeWorld(list1);
  ZTypeWorld ZType4 = new ZTypeWorld(list2);


  // Test bigbang
  boolean testBigBang(Tester t) {
    return ZType3.bigBang(500, 500, 1);
  }



  // Test the makeScene() method
  boolean testMakeScene(Tester t) {
    return t.checkExpect(ZType3.makeScene(), new WorldScene(500, 500) // tests an empty list
        .placeImageXY(new RectangleImage(500, 500, OutlineMode.OUTLINE, Color.BLACK), 250, 250))
        && t.checkExpect(ZType4.makeScene(),
            new WorldScene(500, 500).placeImageXY( // tests a list with one word
                new RectangleImage(500, 500, OutlineMode.OUTLINE, Color.BLACK),
                250, 250).placeImageXY( 
                    new TextImage("", 20, FontStyle.REGULAR, Color.BLUE), 0, 0))
        && t.checkExpect(ZType2.makeScene(), 
            new WorldScene(500, 500).placeImageXY( // tests a list with multiple words
                new TextImage("west-world", 20, FontStyle.REGULAR, Color.BLUE), 0, 0).placeImageXY(
                    new TextImage("hello", 20, FontStyle.REGULAR, Color.BLUE), 0, 0));
  }

  // Test the produceStringTester() method
  boolean testProduceStringTester(Tester t) {
    return t.checkExpect(u1.produceStringTester().equals(u1.produceStringTester()), true)
        && t.checkExpect(u1.produceStringTester(), "rrrrrr");
  }

  // Test the checkAndReduceInavtive(String str) method
  boolean testCheckAndReduceInactive(Tester t) {
    // Test an empty list
    return t.checkExpect(list1.checkAndReduceInactive("x"), new MtLoWord()) &&        
        // Test a list without the letter
        t.checkExpect(list2.checkAndReduceInactive(""), new ConsLoWord(word3, list1)) &&
        // Test a list with the letter
        t.checkExpect(list3.checkAndReduceInactive("w"), new ConsLoWord(word6, list1)) &&
        // Test a list with a word that has multiple occurrences of the letter
        t.checkExpect(list2.checkAndReduceInactive("w"), list2);
  }


  // Test the checkAndReduceActive(String str) method
  boolean testCheckAndReduceActive(Tester t) {
    // Test an empty list
    return t.checkExpect(list1.checkAndReduceActive("x"), new MtLoWord()) &&        
        // Test a list without the letter
        t.checkExpect(list2.checkAndReduceActive(""), new ConsLoWord(word3, list1)) &&
        // Test a list with the letter
        t.checkExpect(list3.checkAndReduceActive("w"),  list3) &&  
        // Test a list with a word that has multiple occurrences of the letter
        t.checkExpect(list2.checkAndReduceActive("w"), list2);
  }  

  // Test the filterOutEmpties() method
  boolean testFilterOutEmpties(Tester t) {
    // Test an empty list
    return t.checkExpect(list1.filterOutEmpties(), list1) &&
        // Test a list containing only an empty word
        t.checkExpect(list2.filterOutEmpties(), list1) &&
        // Test a list not containing an empty word
        t.checkExpect(list3.filterOutEmpties(), list3) &&
        // Test a list that has empty and non empty words
        t.checkExpect(list6.filterOutEmpties(), new ConsLoWord(word5,
            new ConsLoWord(word1,
                new ConsLoWord(word2, list1))));
  }

  // Test the draw() method
  boolean testDraw(Tester t) {

    // Test an empty list
    return t.checkExpect(this.list1.draw(this.scene1), this.scene1) &&
        // Test if words are on the screen
        t.checkExpect(this.list3.draw(this.scene1), new WorldScene(500,500).placeImageXY(
            new TextImage("west-world", 20, Color.BLUE), 0, 0)) &&
        // More tests for active/inactive words
        t.checkExpect(this.list5.draw(this.scene1), new WorldScene(500,500).placeImageXY(
            new TextImage("west-world", 20, Color.BLUE), 0, 0).placeImageXY(
                new TextImage("hello", 20, Color.BLUE), 0, 0).placeImageXY(
                    new TextImage("", 20, Color.BLUE), 0, 0));
  }

  // tests the move() function 
  boolean testMove(Tester t) {
    return t.checkExpect(this.list1.move(), list1)  // tests a list with no words
        && t.checkExpect(this.list2.move(),
            new ConsLoWord(
                new InactiveWord("", 0, 15), list1))  // tests a list with one word
        && t.checkExpect(this.list4.move(), 
            new ConsLoWord(new InactiveWord("hello", 0, 5),
                new ConsLoWord(
                    new InactiveWord("west-world", 0, 5), list1)));
    // tests a list with multiple words
  }


  // tests the function hasActiveWord()
  boolean testHasActiveWord(Tester t) {
    return t.checkExpect(this.list1.hasActiveWord(), false) // tests an empty list
        // tests a list with multiple items but no active word
        && t.checkExpect(this.list2.hasActiveWord(), false)  
        && t.checkExpect(this.list10.hasActiveWord(), true); // tests a list with an active word
  }


  // tests the gameOver function 
  boolean testGameOver(Tester t) {
    return t.checkExpect(this.list1.gameOver(10), false)  // tests an empty list
        && t.checkExpect(this.list3.gameOver(10), false)  // tests a false case
        && t.checkExpect(this.list6.gameOver(10), true) // tests a true case(edge case)
        && t.checkExpect(this.list6.gameOver(1), true); // tests a true case way past

  }

  // tests the lowerCaseWord function
  boolean testLowerCaseWord(Tester t) {
    // tests a word that doesnt need to be changed
    return t.checkExpect(this.word1.lowerCaseWord(), "hello") 
        && t.checkExpect(this.word3.lowerCaseWord(), "")  // tests a empty string
        && t.checkExpect(this.word4.lowerCaseWord(), "fluff"); // tests all caps

  }

  // tests the toImage function
  boolean testToImage(Tester t) {
    return t.checkExpect(this.word1.toImage(new WorldScene(500, 500)),
        new WorldScene(500, 500).placeImageXY(
            new TextImage("hello", 20, FontStyle.REGULAR, Color.BLUE), 0, 0)) 
        // tests a word that doesnt need to be changed
        && t.checkExpect(this.word3.toImage(
            new WorldScene(500, 500)),
            new WorldScene(500, 500).placeImageXY(
                new TextImage("", 20, FontStyle.REGULAR, Color.BLUE), 0, 0)) 
        // tests a empty string
        && t.checkExpect(this.word4.toImage(
            new WorldScene(500, 500)),
            new WorldScene(500, 500).placeImageXY(
                new TextImage("FLUFF", 20, FontStyle.REGULAR, Color.BLUE), 0, 0)) ;
    // tests all caps

  } 

  // tests removeFirst() function
  boolean testRemoveFirst(Tester t) { 
    return t.checkExpect(word1.removeFirst(), new ActiveWord("ello", 0, 0)) // tests a word
        // tests a longer word
        && t.checkExpect(word2.removeFirst(), new ActiveWord("est-world", 0, 0));
  }

  // tests isActive() function
  boolean testIsActive(Tester t) { 
    return t.checkExpect(word1.isActive(), false) // tests a false case
        && t.checkExpect(word6.isActive(), true);// tests a true case
  }

  // tests moveHelper() function
  boolean testMoveHelper(Tester t) { 
    // tests a inactive word
    return t.checkExpect(word1.moveHelper(), new InactiveWord("hello", 0, 5)) 
        // tests a active word
        && t.checkExpect(word6.moveHelper(), new ActiveWord("est-world", 0, 5));
  }


  // tests findWord() function
  boolean testFindWord(Tester t) { 
    return t.checkExpect(word1.findWord(), 0) // tests a word at 0
        && t.checkExpect(word3.findWord(), 10)// tests a word at 10
        && t.checkExpect(word6.findWord(), 0);// tests an active word
  }

  // tests the drawWord function
  boolean testDrawWord(Tester t) {
    // tests an inactive word
    return t.checkExpect(word1.drawWord(), new TextImage("hello", 20, Color.BLUE)) 
        // tests an active word
        && t.checkExpect(word6.drawWord(), new TextImage("est-world", 20, Color.RED));
  }

  // tests lastScene
  boolean testLastScene(Tester t) {
    return t.checkExpect(ZType1.lastScene("Game Over"), new WorldScene(500, 500).placeImageXY(
        new TextImage("Game Over" , 20, FontStyle.REGULAR, Color.RED),
        500 / 2, 500 / 2)) // tests with "Game Over"
        && t.checkExpect(ZType1.lastScene(""), new WorldScene(500, 500).placeImageXY(
            new TextImage("" , 20, FontStyle.REGULAR, Color.RED),
            500 / 2, 500 / 2)); // tests with an empty string
  }



}

