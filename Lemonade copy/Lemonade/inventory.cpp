
#include "inventory.hpp"


inventory::inventory(double m)
{
    money = m;
    lemons = 0;
    ice = 0;
    cups = 0;
    sugar = 0;
    pitcher = 0;
    cupsSold = 0;
    dailyCupsSold = 0;
    fridge = false;
    freezer = false;
    lights = false;
    recipie = false;
    betterStand = false;
    
}
bool inventory::buyLemons(int l)
{
    if(money >= l * PRICE_PER_LEMON_BAG)
    {
        lemons += l * LEMONS_PER_BAG;
        money -= l * PRICE_PER_LEMON_BAG;
        return true;
    }
    return false;
}

bool inventory::buyIce(int i)
{
    if(money >= i * PRICE_PER_ICE_BAG)
    {
        ice += i * ICE_PER_BAG;
        money -= i * PRICE_PER_ICE_BAG;
        return true;
    }
    return false;
}

bool inventory::buySugar(double s)
{
    if(money >= s * PRICE_PER_SUGAR_BAG)
    {
        sugar += s * SUGAR_PER_BAG;
        money -= s * PRICE_PER_SUGAR_BAG;
        return true;
    }
    return false;
}

bool inventory::buyCups(int c)
{
    if(money >= c * PRICE_PER_CUP_BAG)
    {
        cups += c * CUPS_PER_BAG;
        money -= c * PRICE_PER_CUP_BAG;
        return true;
    }
    return false;
}

bool inventory::makePitcher()
{
    if(!recipie)
    {
        if(lemons >= LEMONS_PER_PITCHER && ice >= ICE_PER_PITCHER && sugar >= SUGAR_PER_PITCHER)   //sees if there is enough ingredents to make the pitcher
        {
            lemons -= LEMONS_PER_PITCHER;
            ice -= ICE_PER_PITCHER;
            sugar -= SUGAR_PER_PITCHER;
            pitcher = 4;
            return true;
        }
    }
    else
    {
        if(lemons >= 4 && ice >= 10 && sugar >= 1)   //sees if there is enough ingredents to make the pitcher with the adjusted recipie
        {
            lemons -= 4;
            ice -= 10;
            sugar -= 1;
            pitcher = 4;
            return true;
        }
    }
    return false;
}

bool inventory::sellCup(double price)
{
    if (cups > 0)
    {
        if(pitcher > 0 || makePitcher())
        {
            money += price;
            pitcher--;
            cups--;
            cupsSold++;
            dailyCupsSold++;
            return true;
        }
    }
    return false;
}
void inventory::printInventory()
{
    std::cout << std::endl;
    std::cout << std::endl;
    std::cout << std::showpoint << std::setprecision(3) << "Sugar: " << sugar << std::endl;
    
    std::cout << "Ice cubes:  " << ice << std::endl;
    
    std::cout << "Lemons: " << lemons << std::endl;
    
    std::cout << "Cups: " << cups << std::endl;
    
    std::cout << "Money: $" << money << std::endl;
    
    std::cout << "Total cups sold: " << cupsSold << std::endl;
    
    if(fridge == true)
        std::cout << "You have a fridge: Yes" << std::endl;
    else
        std::cout << "You have a fridge: No" << std::endl;
    if(freezer == true)
        std::cout << "You have a freezer: Yes" << std::endl;
    else
        std::cout << "You have a freezer: No" << std::endl;
    if(lights == true)
        std::cout << "You have lights: Yes" << std::endl;
    else
        std::cout << "You have a lights: No" << std::endl;
    if(recipie == true)
        std::cout << "You have the legendary recipie: Yes" << std::endl;
    else
        std::cout << "You have the legendary recipie: No" << std::endl;
    if(betterStand == true)
        std::cout << "You have a better stand: Yes" << std::endl;
    else
        std::cout << "You have a better stand: No" << std::endl;
}

void inventory::endOfDay()
{
    if(freezer == false)
        ice = 0;
    if(fridge == false)
        pitcher = 0;
    dailyCupsSold = 0;
}

void inventory::printInstruction()
{
    int menu;
    do{
        std::cout << "Welcome to the instructions menu.\nEnter 1 to see basic instructions \nEnter 2 to see pitcher instructions \nEnter 3 to see the store instructions \nEnter 4 to see the objective of the game \nEnter 5 to see the creators note \nEnter 6 to leave the instructions menu ";
        std::cin >> menu;
        if(!std::cin || menu > 6 || menu < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
            while(!(std::cin.peek() == '\n'))
                std::cin.get();                  //clear the stream
    }
    else
        break;
    }while (true);
    
    while(menu != 6)
    {
        if(menu == 1)
            std::cout << std::endl << "   Lemonade is a game where you start with 25 dollars and buy your ingredients from the store so you can set up a lemonade stand. Everyday you will have the option to: see your inventory where it will tell you all your supplies and whether you own specialty items, go the store where you can buy your supplies or specalty items, and set up your lemonade stand. Once you set up your stand you may no longer see your inventory or go to the store for that day. When setting up your stand you will be given the weather forecast for the day. With this information you can set the price of your lemonade accordingly. Depending on the weather more or less people will buy your lemonade. Also, depending on your price more or less people will buy your lemonade, so make sure you charge a reasonable price. \n  If you ever start a day and do not have enough money to buy the supplies that you need to make a pitcher of lemonade, the game will end automatically. \n";
            
        if(menu == 2)
            std::cout << std::endl <<"In this game to sell lemonade you make pitchers of it at a time. A pitcher will automatically be made when you have an empty pitcher, UNLESS you do not have enough ingredients to make the pitcher. Each pitcher can hold 4 cups of lemonade before it is empty. To make a pitcher you need: \n1.5 cups of sugar \n6 lemons \n12 cubes of ice\nAt the end of each day your pitcher will be emptied unless you have a fridge that you can keep your pitcher in.\n";
            
        if(menu == 3)
            std::cout << std::endl <<"   The store has two seperate stores inside which both sell different things. The grocery store sells all the supplies you need to sell lemonade. (lemons, sugar, ice, and cups) Whereas Foster's specialty store sells specialty items that will help your business succeed. \n   At the grocery store it will ask you an interger amount that you want to buy, but at the specialty store it will just ask if you want to buy it or not. Also, at the grocery store it will give youe the price, the quantity, the amount per pitcher, and the amount if you have the legendary recipie. Remember, PP will refer to the amount that you need to make one pitcher. While PPLR is refering to the amount that you need  to make a pitcher with the legendary recipie.\n";
            
        if(menu == 4)
            std::cout << std::endl <<"  The objective of the game is to finish with the most amount of money after 10 days. You make money buy setting the price right to maxamize your sales.\n";
            
        if(menu == 5)
            std::cout << std::endl <<"   Hi, my name is George Foster and I created this version of lemonade. The orginal concept and game came from Mr. Brusko's Lemonade app that might be able to get from the app store. If you find it make sure you spend the $0.99 to get the app. Some fun facts about me, I love to add easter eggs and I heard that if you enter 22 when it asks you if you want to play the game or exit the game, then you will start with 100 dollars instead of 25. Good luck. \n";
            
        do{
               std::cout << std::endl << "\nEnter 1 to see basic instructions \nEnter 2 to see pitcher instructions \nEnter 3 to see the store instructions \nEnter 4 to see the objective of the game \nEnter 5 to see the creators note \nEnter 6 to leave the instructions menu ";
               std::cin >> menu;
               if(!std::cin || menu > 6 || menu < 1){ std::cout << "Bad input\n";
                   std::cin.clear();                    //reset fail state
                   while(!(std::cin.peek() == '\n'))
                       std::cin.get();                  //clear the stream
           }
           else
               break;
           }while (true);
    }
}

int inventory::getDailyCupsSold()
{
    return dailyCupsSold;
}


bool inventory::checkMoneyToPlay()
{
    if(money < 1.5)
        return false;
    else if(lemons < 6 && money < 4.50)
        return false;
    else if(sugar < 1.5 && money < 3.99)
        return false;
    else if(cups < 1 && money < 2.49)
        return false;
    else
        return true;
}

void inventory::easterEgg()
{
    money += 75;
                          
}

void inventory::buyFridge()
{
    int yesOrNo;
    if(fridge == false)
    {
        do{
        std::cout << "\n\nWould you like to rent a fridge for 10 dollars? \nInstead of throwing out your pitchers at the end of the day you would keep them in your fridge so you could save a few cups of lemonade. \nYou would have access to this perk for the rest of the game.\nEnter 1 to buy it and 2 to not buy it.";
        std::cin >> yesOrNo;
            if(!std::cin || yesOrNo > 2 || yesOrNo < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
        while(!(std::cin.peek() == '\n'))
            std::cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
        if(yesOrNo == 1)
        {
            if(money < 10)
                std::cout << "\nUnforutunalty you do not have enough money to buy the fridge.\nSorry\n";
            else
            {
                money -= 10;
                fridge = true;
            }
        }
    }
}

void inventory::buyFreezer()
{
    int yesOrNo;
    if(freezer == false)
    {
        do{
        std::cout << "\n\nWould you like to rent a freezer for 12 dollars? \nInstead of your ice melting at the end of the day you would keep it in your freezer so you could save all your ice. \nYou would have access to this perk for the rest of the game.\nEnter 1 to buy it and 2 to not buy it.";
        std::cin >> yesOrNo;
            if(!std::cin || yesOrNo > 2 || yesOrNo < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
        while(!(std::cin.peek() == '\n'))
            std::cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
        if(yesOrNo == 1)
        {
            if(money < 12)
                std::cout << "\nUnforutunalty you do not have enough money to buy the freezer.\nSorry\n";
            else
            {
                money -= 10;
                freezer = true;
            }
        }
    }
}

int inventory::getLemons()
{
    return lemons;
}
int inventory::getCupsSold()
{
    return cupsSold;
}
double inventory::getSugar()
{
    return sugar;
}
int inventory::getMoney()
{
    return money;
}
int inventory::getCups()
{
    return cups;
}

void inventory::lightningStorm()
{
    cups = 0;
    sugar = 0;
    lemons = 0;
    ice = 0;
    
}

void inventory::buyLights()
{
    int yesOrNo;
    if(lights == false)
    {
        do{
        std::cout << "\n\nWould you like to rent party lights for 30 dollars? \nInstead of closing your stand at sunset you can keep it open until 10:00 PM. \nYou would have access to this perk for the rest of the game. \nEnter 1 to buy it and 2 to not buy it.";
        std::cin >> yesOrNo;
            if(!std::cin || yesOrNo > 2 || yesOrNo < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
        while(!(std::cin.peek() == '\n'))
            std::cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
        if(yesOrNo == 1)
        {
            if(money < 30)
                std::cout << "\nUnforutunalty you do not have enough money to buy the lights.\nSorry\n";
            else
            {
                money -= 30;
                lights = true;
            }
        }
    }
}

void inventory::buyBetterStand()
{
    int yesOrNo;
    if(betterStand == false)
    {
        do{
        std::cout << "\n\nWould you like to rent a better stand that will atract more people for $20? \nThis would attract more people to your stand. \nYou would have access to this perk for the rest of the game. \nEnter 1 to buy it and 2 to not buy it.";
        std::cin >> yesOrNo;
            if(!std::cin || yesOrNo > 2 || yesOrNo < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
        while(!(std::cin.peek() == '\n'))
            std::cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
        if(yesOrNo == 1)
        {
            if(money < 20)
                std::cout << "\nUnforutunalty you do not have enough money to the stand.\nSorry\n";
            else
            {
                money -= 20;
                betterStand = true;
            }
        }
    }
}

void inventory::buyBetterRec()
{
    int yesOrNo;
    if(recipie == false)
    {
        do{
        std::cout << "\n\nWould you like to buy the legendary lemonade recipie for $35? \nThis recipie would make it so that you could make a pitcher with 4 lemons, 1 cup of sugar, and 10 ice cubes.\nYou would have access to this perk for the rest of the game. \nEnter 1 to buy it and 2 to not buy it.";
        std::cin >> yesOrNo;
            if(!std::cin || yesOrNo > 2 || yesOrNo < 1){ std::cout << "Bad input\n";
            std::cin.clear();                    //reset fail state
        while(!(std::cin.peek() == '\n'))
            std::cin.get();                  //clear the stream
        }
        else
            break;
        }while (true);
        if(yesOrNo == 1)
        {
            if(money < 35)
                std::cout << "\nUnforutunalty you do not have enough money to buy the legendary recipie.\nSorry\n";
            else
            {
                money -= 35;
                recipie = true;
            }
        }
    }
}

bool inventory::getLights()
{
    return lights;
}
bool inventory::getBetterRec()
{
    return recipie;
}
bool inventory::getBetterStand()
{
    return betterStand;
}

void inventory::printEndOfDaySpecialtyItems()
{
    if(fridge == true)
        std::cout << "You have a fridge: Yes" << std::endl;
    else
        std::cout << "You have a fridge: No" << std::endl;
    if(freezer == true)
        std::cout << "You have a freezer: Yes" << std::endl;
    else
        std::cout << "You have a freezer: No" << std::endl;
    if(lights == true)
        std::cout << "You have lights: Yes" << std::endl;
    else
        std::cout << "You have a lights: No" << std::endl;
    if(recipie == true)
        std::cout << "You have the legendary recipie: Yes" << std::endl;
    else
        std::cout << "You have the legendary recipie: No" << std::endl;
    if(betterStand == true)
        std::cout << "You have a better stand: Yes" << std::endl;
    else
        std::cout << "You have a better stand: No" << std::endl << std::endl;
    
}
