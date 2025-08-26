#ifndef inventory_hpp
#define inventory_hpp

#include <iostream>
#include "constant.h"
#include <iomanip>

class inventory {
    
private:
    
    double money;
    int lemons;
    int ice;
    double sugar;
    int cups;
    int pitcher;  //how many cups of lemonade or in the pitcher to sell (0-4)
    bool makePitcher();
    int cupsSold; //how many cups the stand has sold
    int dailyCupsSold;
    bool freezer;
    bool fridge;
    bool recipie;
    bool lights;
    bool betterStand;
    
    
public:
    inventory(double);
    bool buyLemons(int);
    bool buyIce(int);
    bool buySugar(double);
    bool buyCups(int);
    void printInventory();
    bool sellCup(double); //price your selling cup for is the double
    void endOfDay(); //to set ice and pitcher to 0
    void printInstruction();
    int getDailyCupsSold();
    bool checkMoneyToPlay();
    void easterEgg();
    void buyFreezer();
    void buyFridge();
    int getLemons();
    int getCupsSold();
    double getSugar();
    int getMoney();
    int getCups();
    void lightningStorm();
    void buyLights();
    void buyBetterStand();
    void buyBetterRec(); // to buy a recipie that makes the same amount of pitchers but uses less ingredients
    bool getLights();  // these next three all return true or false depending if the player own the perks
    bool getBetterStand();
    bool getBetterRec();
    void printEndOfDaySpecialtyItems();
};

#endif /* inventory_hpp */
