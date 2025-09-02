#include <iostream>
#include "inventory.hpp"
#include <fstream>

using namespace std;

int temp();
string weather(inventory&);
double idealPricePerCup(int, string);
int findFootTraffic(int, string, inventory&);
void printEndDay(double, double, int, int);
void sellingCups(int, double, double, inventory&);
void store(inventory&);
void printEndOfGame(inventory&);
void lightningStorm(inventory&);
void homeGoodsStore(inventory&);
void printHighScores(string, double);



int main()
{
    int mainMenu;
    inventory player(25); //contructing class / need this here to print insructions
    int tempForDay, footTraffic; //temp for day is the tempature for each day and footTraffic is how many people will walk past yor stand
    double idealPrice;  //the ideal price for lemonade to maxamize the revenue
    string weatherForDay; // weather for the day ex. sunny
    double playersPrice; //price for lemoade
    int storeInventoryStart; // chosing the options in a menu
    int lightning;
    int storeType; // go to home store or go to the grocery store
    string standName;
    char extraSpace;
    
    cout << "Hello, welcome to Lemonade! \nLemonade is a game where you set up a lemonade stand and see how many cups you can sell! \n";
    do{
        cout << "Enter 1 to view the instuctions. \nEnter 2 to start the game. \nEnter 3 to exit the game. \n";
        cin >> mainMenu;
        if(!cin || mainMenu > 3 || mainMenu < 1){ cout << "Bad input" << endl;
            cin.clear();                    //reset fail state
            while(!(cin.peek() == '\n'))
                cin.get();                  //clear the stream
     }
     else
        break;
    }while (true);
    
    if(mainMenu == 1)
    {
        player.printInstruction();
        do{
            cout << "Now that you know how Lemonade works, would you like to play the game(enter 2) or exit the game(enter 3)";
            cin >> mainMenu;
            if(mainMenu == 22)
            {
                player.easterEgg();
                mainMenu = 2;
                cout << "You now have 100 dollars!" << endl << endl;
            }
            if(!cin || mainMenu > 3 || mainMenu < 2){ cout << "Bad input" << endl;
                cin.clear();                    //reset fail state
                while(!(cin.peek() == '\n'))
                    cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
    }
    
    if(mainMenu == 2)
    {
        do{
            cout << "Welcome to the lemonade game! what is the name of your stand? (ex. _Sally's Stand, _Freshest Lemonade Stand)(Make sure you put a underscore before your stand name)";
            cin >> extraSpace; // making it so that the user has to enter something otherwise it would wait for the user to enter something and it would just the empty string
            getline(cin, standName);
            cout << endl;
            if(extraSpace != '_'){ cout << "Bad input" << endl;
                cin.clear();                    //reset fail state
                        //clear the stream
        }
        else
            break;
        }while (true);
        
        for(int day = 1; day < 11; day++) //every time one loop finishes one day will have finished
        {
            if(player.checkMoneyToPlay() == false)
            {
                cout << "\n\n\nYou do not have enough money to keep playing. \n Please comeback and play again some time soon. \n You lost on day " << day - 1 << endl << endl;
                break;
            }
            //play game code goes here
            do{
               cout << "Hi, welcome to day " << day << " at " << standName << "! \nWhat is the first thing you want do to do?\nEnter 1 to go to the store. \nEnter 2 to see your inventory. \nEnter 3 to set up the lemonade stand.\n";
               cin >> storeInventoryStart;
                if(!cin || storeInventoryStart > 3 || storeInventoryStart < 1){ cout << "Bad input" << endl;
                    cin.clear();                    //reset fail state
                    while(!(cin.peek() == '\n'))
                        cin.get();                  //clear the stream
            }
            else
                break;
            }while (true);

           while(storeInventoryStart != 3)
           {
               if(storeInventoryStart == 1)
               {
                   do{
                   cout << "\nHi welcome to the store. Would you like to go to the grocery store (enter 1) or Foster's specialty store (enter 2)?\n";
                   cin >> storeType;
                   if(!cin || storeType > 2 || storeType < 1){ cout << "Bad input" << endl;
                       cin.clear();                    //reset fail state
                   while(!(cin.peek() == '\n'))
                       cin.get();                  //clear the stream
                   }
                   else
                       break;
                   }while (true);
                   
                   if(storeType == 1)
                       store(player);
                   else
                       homeGoodsStore(player);
               }
                
               if(storeInventoryStart == 2)
                   player.printInventory();
               
               do{
               cout << "\nNext would you like to: \nGo to the store(enter 1). \nSee your inventory(enter 2). \nSet up the lemonade stand(enter 3).\n";
               cin >> storeInventoryStart;
               if(!cin || storeInventoryStart > 3 || storeInventoryStart < 1){ cout << "Bad input" << endl;
                   cin.clear();                    //reset fail state
               while(!(cin.peek() == '\n'))
                   cin.get();                  //clear the stream
               }
               else
                   break;
               }while (true);
               
           }
            tempForDay = temp();
            weatherForDay = weather(player);
            idealPrice = idealPricePerCup(tempForDay, weatherForDay);
            footTraffic = findFootTraffic(tempForDay, weatherForDay, player);
            
            cout << "The weather forcast for the day is: " << tempForDay << " and " << weatherForDay << "." << endl;
            
            do{
            cout << "How much would you like to sell a cup of lemonade for? (Ex. 1.39) ";
            cin >> playersPrice;
                if(!cin || playersPrice < 0){ cout << "Bad input" << endl;
                cin.clear();                    //reset fail state
            while(!(cin.peek() == '\n'))
                cin.get();                  //clear the stream
            }
            else
                break;
            }while (true);
            if(weatherForDay == "rainy")
            {
                lightning = arc4random_uniform(3);
                if(lightning == 1)
                {
                    lightningStorm(player);
                    break;
                }
            }
            
            sellingCups(footTraffic, playersPrice, idealPrice, player);
            printEndDay(idealPrice, playersPrice, footTraffic, player.getDailyCupsSold());
            player.endOfDay();
        }
        printEndOfGame(player);
       //printHighScores(standName, player.getMoney());
    }

    if(mainMenu == 3)
        cout << "Thank you! \n Please come back another time to play the game! \n";
    
    return 0;
}

int temp()
   {
       int weather = arc4random_uniform(40) + 61;
       return weather;
   }

string weather(inventory& player)
{
    int x = arc4random_uniform(50);
    string weather;
    if(x <= 25)
        return "sunny";
    else if(x <= 30 && x > 25)
        return "rainy";
    else if(x <= 40 && x > 30)
        return "humid";
    else
        return "cloudy";
}

double idealPricePerCup(int temp, string weather)
{
    double weatherInt;
    double tempInt;
    if(weather  == "sunny")
        weatherInt = .7;
    else if(weather  == "humid")
        weatherInt = 1;
    else if(weather  == "cloudy")
        weatherInt = .5;
    else
        weatherInt = .2;
    
    if(temp > 70 && temp< 81)
        tempInt = .9;
    else if (temp > 80 && temp< 86)
        tempInt = 1.2;
    else if (temp > 85 && temp< 91)
        tempInt = 1.5;
    else if (temp > 90 && temp< 94)
        tempInt = 1.8;
    else if (temp > 93 && temp< 96)
        tempInt = 2;
    else if (temp > 95 && temp< 98)
        tempInt = 2.3;
    else if (temp == 100)
        tempInt = 2.5;
    else
        tempInt = .7;
    return weatherInt * tempInt + CHEAPEST_CUP_PRICE;
}

int findFootTraffic(int temp, string weather, inventory& player)
{
    int footTraffic;
    double weatherImpact;
    if(weather  == "sunny")
        weatherImpact = 1.2;
    else if(weather  == "humid")
        weatherImpact = 1.7;
    else if(weather  == "cloudy")
        weatherImpact = .7;
    else
        weatherImpact = .3;
    
    footTraffic = temp * weatherImpact * 3/7;
    if(player.getLights() == true)
        footTraffic = footTraffic * 1.35;
    if(player.getBetterStand() == true)
        footTraffic = footTraffic * 1.2;
    
    return footTraffic;
}

void printEndDay(double idealPrice, double playersPrice, int footTraffic, int dailyCupsSold)
{
    cout << endl << "End of day!" << endl << endl;
    
    cout << showpoint << setprecision(3) << "The ideal price was $" << idealPrice << endl;
    
    cout << "The amount of people that walked by your stand was: " << footTraffic << " people." << endl;
    
    cout << showpoint << setprecision(3) << "If you set the price right, you could have made a total of: $" << footTraffic * idealPrice << endl;
    cout << "Today you sold " << dailyCupsSold << " cups" << endl << endl;
    
}

void sellingCups(int footTraffic, double playersPrice, double idealPrice, inventory& player)
{
    //this next loop will run through a loop for every person and if there price is lower than it will sell the cup but if higher it will do like a 50 percent chance or 1/3 percent chance
    for(int x = 0; x < footTraffic; x++)
    {
        if(playersPrice <= idealPrice)
        {
            if(!player.sellCup(playersPrice))
            {
                cout << endl << "YOU HAVE SOLD OUT." << endl;
                break;
            }
        }
        else if(playersPrice > idealPrice && playersPrice <= idealPrice + .15)
        {
            if(arc4random_uniform(2) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + .15 && playersPrice <= idealPrice + .5)
        {
            if(arc4random_uniform(3) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + .5 && playersPrice <= idealPrice + .8)
        {
            if(arc4random_uniform(4) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl << "YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + .8 && playersPrice <= idealPrice + 1)
        {
            if(arc4random_uniform(5) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + 1 && playersPrice <= idealPrice + 1.5)
        {
            if(arc4random_uniform(6) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + 1.5 && playersPrice <= idealPrice + 3)
        {
            if(arc4random_uniform(9) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl << "YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else if(playersPrice > idealPrice + 3 && playersPrice <= idealPrice + 15)
        {
            if(arc4random_uniform(15) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        else
        {
            if(arc4random_uniform(1000) == 1)
            {
                if(!player.sellCup(playersPrice))
                {
                    cout << endl <<"YOU HAVE SOLD OUT." << endl;
                    break;
                }
            }
        }
        
    }
}

void store(inventory& player)
{
    int lemonBags, iceBags, sugarBags, cupBags; //how many things there buying
    cout << "Welcome to the store. The store is sells lemons, sugar, cups, and ice. We will now go through one product at a time. Also, please remeber PP means per pitcher and PPLR means per pitcher with the legendary recipie.\n\n";
                   //selling lemons
                   do{
                       cout << "How many bags of lemons would you like to buy? (10 lemons per bag)(cost $3.00 per bag)(6 lemons PP)(4 lemons PPLR)";
                       cin >> lemonBags;
                       if(!cin){ cout << "Bad input" << endl;
                           cin.clear();                    //reset fail state
                           while(!(cin.peek() == '\n'))
                               cin.get();                  //clear the stream
                   }
                   else
                       break;
                   }while (true);
                   while(lemonBags < 0|| player.buyLemons(lemonBags) == false)
                   {
                       do{
                            cout << "You either do not have enough money to buy that or it is an invalid input.\n How many bags of lemons would you like to buy? (10 lemons per bag)(cost $3.00 per bag)(6 lemons PP)(4 lemons PPLR)";
                            cin >> lemonBags;
                            if(!cin){ cout << "Bad input" << endl;
                                cin.clear();                    //reset fail state
                            while(!(cin.peek() == '\n'))
                                cin.get();                  //clear the stream
                                         }
                            else
                                break;
                            }while (true);
                   }
                   //selling sugar
                   do{
                   cout << "\nHow many bags of sugar would you like to buy? (5 cups of sugar per bag)(cost $2.49 per bag)(1.5 cups PP)(1 cup PPLR)";
                   cin >> sugarBags;
                   if(!cin){ cout << "Bad input" << endl;
                       cin.clear();                    //reset fail state
                   while(!(cin.peek() == '\n'))
                       cin.get();                  //clear the stream
                                }
                   else
                       break;
                   }while (true);
                   while(sugarBags < 0|| player.buySugar(sugarBags) == false)
                   {
                       do{
                       cout << "You either do not have enough money to buy that or it is an invalid input.\nHow many bags of sugar would you like to buy? (5 cups of sugar per bag)(cost $2.49 per bag)(1.5 cups PP)(1 cup PPLR)";
                       cin >> sugarBags;
                       if(!cin){ cout << "Bad input" << endl;
                           cin.clear();                    //reset fail state
                       while(!(cin.peek() == '\n'))
                           cin.get();                  //clear the stream
                       }
                       else
                           break;
                       }while (true);
                   }
                   //selling cups
                   do{
                   cout << "\nHow many bags of cups would you like to buy? (50 cups per bag)(cost $0.99 per bag)(1 cup per sale)";
                   cin >> cupBags;
                   if(!cin){ cout << "Bad input" << endl;
                       cin.clear();                    //reset fail state
                   while(!(cin.peek() == '\n'))
                       cin.get();                  //clear the stream
                   }
                   else
                       break;
                   }while (true);
                   while(cupBags < 0|| player.buyCups(cupBags) == false)
                   {
                       do{
                       cout << "You either do not have enough money to buy that or it is an invalid input.\nHow many bags of cups would you like to buy? (50 cups per bag)(cost $0.99 per bag)(1 cup per sale)";
                       cin >> cupBags;
                       if(!cin){ cout << "Bad input" << endl;
                           cin.clear();                    //reset fail state
                       while(!(cin.peek() == '\n'))
                           cin.get();                  //clear the stream
                       }
                       else
                           break;
                       }while (true);
                   }
                   //selling ice
                   do{
                   cout << "\nHow many bags of ice would you like to buy? (150 ice cubes per bag)(cost $1.50 per bag)(12 cubes PP)(10 cubes PPLR)";
                   cin >> iceBags;
                   if(!cin){ cout << "Bad input" << endl;
                       cin.clear();                    //reset fail state
                   while(!(cin.peek() == '\n'))
                       cin.get();                  //clear the stream
                   }
                   else
                       break;
                   }while (true);
                   while(iceBags < 0|| player.buyIce(iceBags) == false)
                   {
                       do{
                       cout << "You either do not have enough money to buy that or it is an invalid input.\nHow many bags of ice would you like to buy? (150 ice cubes per bag)(cost $1.50 per bag)(12 cubes PP)(10 cubes PPLR)";
                       cin >> iceBags;
                       if(!cin){ cout << "Bad input" << endl;
                           cin.clear();                    //reset fail state
                       while(!(cin.peek() == '\n'))
                           cin.get();                  //clear the stream
                       }
                       else
                           break;
                       }while (true);
                   }
}

void printEndOfGame(inventory& player)
{
    cout << "\nGame Over.\n" << endl;
    cout << "Here is your inventory: \n";
    cout << "Cups: " << player.getCups() << endl;
    cout << "Lemons: " << player.getLemons() << endl;
    cout << "Sugar: " << player.getSugar() << endl << endl;
    cout << "Here are your stand stats: \n";
    cout << "Total Cups Sold: " << player.getCupsSold() << endl;
    cout << "Average cups sold per day: " << player.getCupsSold() / 10 << endl;
    cout << "Money that you have: " << player.getMoney() << endl << endl;
    cout << "Specialty Items: " << endl;
    player.printEndOfDaySpecialtyItems();
    
}

void lightningStorm(inventory& player)
{
    cout << "\n\nOh no, YOUR STAND HAS BEEN STRUCK BY LIGHTNING!\n\nAll of your inventory (ice, lemons, sugar, and cups) has been destroyed by the lightning.\nAlso, the lightning strike was fatal to your avatar. Sadly he has passed away so your game has ended. \n";
    
    cout << "    __" << endl;
    cout << "   / /" << endl;
    cout << "  / /___" << endl;
    cout << "  |__  /" << endl;
    cout << "    / /" <<  endl;
    cout << "   /_/" <<  endl;
    
    player.lightningStorm();
}

void homeGoodsStore(inventory& player)
{
    player.buyFreezer();
    player.buyFridge();
    player.buyLights();
    player.buyBetterStand();
    player.buyBetterRec();
}

void printHighScores(string standName, double money)
{
    ifstream inData;
    ofstream outData;
    string stand1, stand2, stand3;
    double score1 = 0, score2 = 0, score3 = 0;
    inData.open("lemonadeTextOut.txt");
    outData.open("lemonadeTextOut.txt");
    
    inData >> score1 >> stand1;
    inData >> score2 >> stand2;
    inData >> score3 >> stand3;
  
    score3 = score2;
    score2 = score1;
    score1 = money;
    
    stand3 = stand2;
    stand2 = stand1;
    stand1 = standName;
    
    cout << "Most recent games: " << endl;
    cout << stand1 << "- "<< score1 << endl;
    cout << stand2 << "- "<< score2 << endl;
    cout << stand3 << "- "<< score3 << endl;

    cout << "The recent games show the most recent persons stnad name and teh money they ended with." << endl;
    
    outData << score1 << " " << stand1 << endl;
    outData << score2 << " " << stand2 << endl;
    outData << score3 << " " << stand3 << endl;

    inData.close();
    outData.close();
    
}
