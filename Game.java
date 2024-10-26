import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.InputStream;
import javax.swing.ImageIcon;
import java.io.IOException;


/**
 * This class is the main class of the "Moonlit Marrakech Hotel" application. 
 * "Moonlit Marrakech Hotel" is a text based adventure game. Users 
 * can walk around some scenery, hold, drop and give items. 
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns. 
 * 
 * @author  Michael Kölling, David J. Barnes and Aïda Tadlaoui
 * @version 2023.12.06
 */

public class Game 
{
    private Parser parser;
    private Player player;
    
    private List<Room> roomVisited;
    private List<Room> allRooms;
    private List<Item> givenItems;
    
    private Character janitor;
    private Character chef;
    private Character shopkeeper;
    
    private Room currentRoom;
    private Room previousRoom;

    /**
     * Create the game and initialise its internal structure.
     */
    public Game() 
    {
        parser = new Parser();
        this.player = new Player();
        
        roomVisited = new ArrayList<>();
        allRooms = new ArrayList<>();
        givenItems = new ArrayList<>();
        
        // create mobile characters in the game
        shopkeeper = new Character("The shop keeper");
        janitor = new Character("The janitor");
        chef = new Character("The chef");
        
        createRooms();
        displayMap();
    }

    /**
     * Create and instantiate the rooms and their exits.
     */
    private void createRooms()
    {
        Room garden, shop, bathroom, lounge, reception, restaurant, kitchen, conference, library, luggage, magic;
        
        // create rooms and adding them in the allRooms list
        garden = new Room("in the garden");
        allRooms.add(garden);
        reception = new Room("in the reception");
        allRooms.add(reception);
        janitor.setCurrentRoom(reception);
        restaurant = new Room("in the restaurant");
        allRooms.add(restaurant);
        luggage = new Room("in the luggage storage");
        allRooms.add(luggage);
        kitchen = new Room("in the kitchen");
        allRooms.add(kitchen);
        chef.setCurrentRoom(kitchen);
        lounge = new Room("in the lounge");
        allRooms.add(lounge);
        bathroom = new Room("in the bathroom");
        allRooms.add(bathroom);
        library = new Room("in the library");
        allRooms.add(library);
        shop = new Room("in the souvenir shop");
        allRooms.add(shop);
        shopkeeper.setCurrentRoom(shop);
        conference = new Room("in the conference room");
        allRooms.add(conference);
        magic = new Room("in the magic room");
        
        // initialise room exits
        garden.setExit("east", reception);
        garden.setExit("south", lounge);
        garden.setExit("north", restaurant);
        garden.setExit("west", library);

        reception.setExit("south", luggage);
        reception.setExit("north", bathroom);
        reception.setExit("west", garden);

        restaurant.setExit("east", bathroom);
        restaurant.setExit("south", garden);
        restaurant.setExit("north", kitchen);

        luggage.setExit("north", reception);
        luggage.setExit("west", lounge);

        kitchen.setExit("south", restaurant);
        
        lounge.setExit("east", luggage);
        lounge.setExit("south", conference);
        lounge.setExit("north", garden);
        
        bathroom.setExit("south", reception);
        bathroom.setExit("west", restaurant);
        
        library.setExit("south", magic);
        library.setExit("east", garden);
        library.setExit("west", shop);
        
        shop.setExit("east", library);
        
        conference.setExit("north", lounge);
        
        magic.setExit("north", library);
        
        currentRoom = bathroom;  // start the game of the player in the bathroom
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing, hope you enjoyed it ! Good bye !");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Morocco in the Moonlit Marrakech Hotel.");
        System.out.println();
        System.out.println("A violent earthquake has just struck Morocco !");
        System.out.println("You are the main character in this game. Young and fit, you have the ability to escape and survive the earthquake.");
        System.out.println("However, three characters in the game are in distress in different rooms of the hotel, and need your help.");
        System.out.println("Each of them requires a specific item to escape and survive.");
        System.out.println("Your mission is to bring the designated items to each person according to their unique needs, and then seek refuge in the garden.");
        System.out.println("The King of Morocco is counting on you !");
        System.out.println();
        System.out.println("You have a hotel floor plan available to help you navigate more easily within the hotel.");
        System.out.println("Warning ! You have a weight limit for carrying items.");
        System.out.println("And if you drop the items, please remember in which room you dropped them.");
        System.out.println("The situation is critical. You don't have time to search throughout the entire hotel for an item you may have dropped in a room.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println(currentRoom.getCharactersRoom());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("hold")) {
            hold(command);
        }
        else if (commandWord.equals("back")) {
            goBack();
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("give")) {
            give(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print helpful message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You need to help those three characters : the mother, the old man and the visually impaired person.");
        System.out.println();
        System.out.println("The mother needs her baby.");
        System.out.println("The old man needs his cane.");
        System.out.println("The visually impaired person needs their glasses.");
        System.out.println();
        System.out.println("To give an item to a character, make sure to use these types of commands : give item character.");
        System.out.println("For the character one you can use : mother, oldman or person.");
        System.out.println();
        System.out.println("Good luck !");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            roomVisited.add(currentRoom);
            currentRoom = nextRoom;
            
            // display a complete description of the last room (exits, characters, items, movements of mobile characters)
            System.out.println();
            System.out.println(currentRoom.getLongDescription());
            System.out.println();
            System.out.println(currentRoom.getCharactersRoom());
            System.out.println("• Movements of mobile characters :");
            moveCharacterRandomly(chef);
            moveCharacterRandomly(janitor);
            moveCharacterRandomly(shopkeeper);
            System.out.println();
            
            checkMagicWord(); // checks if the player enter the magic room
            checkVictory(); // checks if the player has won
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Takes the player back to the last room he is been in.
     */
    private void goBack()
    {
        if(roomVisited.size() > 0) {
            currentRoom = roomVisited.get(roomVisited.size() - 1);
            roomVisited.remove(roomVisited.size() - 1);
            
            // display a complete description of the last room (exits, characters, items, movements of mobile characters)
            System.out.println();
            System.out.println(currentRoom.getLongDescription());
            System.out.println();
            System.out.println(currentRoom.getCharactersRoom());
            System.out.println("Movements of mobile characters:");
            moveCharacterRandomly(chef);
            moveCharacterRandomly(janitor);
            moveCharacterRandomly(shopkeeper);
            System.out.println();
        }
        else {
            System.out.println("Oops,can't go back any further!");
        }
    } 

    /**
      * Allows the player to carry an item.
      * @param command The command to be processed.
      */
    private void hold(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Hold what?");
            return;
        }
        
        String itemName = command.getSecondWord();
        Item foundItem = null;
        boolean itemFound = false;
        
        // Search if the item the player wants to hold is in the current room
        for (Item item : currentRoom.getItems()) {
            if (item.getItemName().equals(itemName)) {
                foundItem = item;
                itemFound = true;
                break;
            }
        }
        
        // Checks if the found item is an item that can be hold (glasses, cane or baby)
        if (itemFound) { 
            if ((itemName.equals("glasses") || itemName.equals("cane") || itemName.equals("baby"))) {
                if (player.canTakeItem(foundItem)) {
                    foundItem.setCarried(true);
                    foundItem.setDropped(false);
                    currentRoom.removeItems(foundItem);
                    System.out.println("You are holding the " + itemName + ".");
                } else {
                    System.out.println("Can't take this item. Weight limit exceeded");
                }
            } else {
                System.out.println("You can't hold this item.");
            } 
        } else {
            System.out.println("Item not found.");
        } 
    }
    
    /**
      * Drop an item that the player is holding.
      * @param command The command to be processed.
      */
    private void drop(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
        
        String itemName = command.getSecondWord();
        
        // Checks if the item the player wants to drop is carried and from its inventory
        if ((itemName.equals("glasses") || itemName.equals("cane") || itemName.equals("baby")) && player.isCarrying(itemName)) {
            Item itemToDrop = null;
            
            for (Item item : player.getInventory()) {
                if (item.getItemName().equals(itemName)) {
                    itemToDrop = item;
                    break;
                }
            }
            
            // Drop the item in the current room
            if (itemToDrop != null) {
                currentRoom.addItems(itemToDrop);
                player.dropItem(itemToDrop);
            }
        } else {
            System.out.println("Item not found or you are not carrying the " + itemName + ".");
        }
    }
    
    /**
      * Give an item to a character.
      * @param command The command to be processed.
      */
    private void give(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Give what?");
            return;
        }
        
        if (!command.hasThirdWord()) {
            System.out.println("Give the item to whom?");
            return;
        }
        
        String itemName = command.getSecondWord();
        String characterName = command.getThirdWord();
         
        // Checks if the item the player wants to give is carried and from its inventory
        if ((itemName.equals("glasses") || itemName.equals("cane") || itemName.equals("baby")) && player.isCarrying(itemName)) {
            Item itemToGive = null;
            for (Item item : player.getInventory()) {
                if (item.getItemName().equals(itemName)) {
                    itemToGive = item;
                    break;
                }
            }
            
            // Checks if the item the player wants to give matches the one needed by the character and if this character is in the same room as the player
            if ((characterName.equals("person") || characterName.equals("mother") || characterName.equals("oldman"))) {
                if (itemToGive != null && characterName != null) {
                    if (itemName.equals("glasses") && characterName.equals("person") && currentRoom.getCharactersRoom().contains("person")) {
                        player.giveItem(itemToGive);
                        givenItems.add(itemToGive);
                        System.out.println("• You have given the " + itemName + " to the visually impaired person.");
                    } else if (itemName.equals("baby") && characterName.equals("mother") && currentRoom.getCharactersRoom().contains("mother")) {
                        player.giveItem(itemToGive);
                        givenItems.add(itemToGive);
                        System.out.println("• You have given the " + itemName + " to the mother.");
                    } else if (itemName.equals("cane") && characterName.equals("oldman") && currentRoom.getCharactersRoom().contains("old man")) {
                        player.giveItem(itemToGive);
                        givenItems.add(itemToGive);
                        System.out.println("• You have given the " + itemName + " to the old man.");
                    } else {
                        System.out.println("This character is not present.");
                    }
                }
            } else {
                System.out.println("This character does not need that item.");
            }
        }
    }
    
    /**
    * Moves the character randomly within the available exits of their current room.
    * @param character The character to be moved randomly.
    */
    private void moveCharacterRandomly(Character character) {
        Random random = new Random();
        previousRoom = character.getCurrentRoom();
        String[] availableExits = previousRoom.getAvailableExit();
        String randomDirection = availableExits[random.nextInt(availableExits.length)];
        Room nextRoom = previousRoom.getExit(randomDirection);
        
        if (nextRoom != null) {
            character.setCurrentRoom(nextRoom);
            System.out.println("- " + character.getCharacterName() + " moved " + nextRoom.getShortDescription());
        }
    }
    
    /**
      * Return a list containing the rooms of the game (apart from the magic room).
      * @return List of the rooms in the game.
      */
    private List<Room> getRooms(){
        return allRooms;
    }
    
    /**
     * Return a list containing the items that the player gives.
     * @return List of the items that the player gives.
     */
    private List<Item> getGivenItems() {
        return givenItems;
    }
    
    /**
      * Teleport randomly if the player is in the magic room. 
      */
    private void checkMagicWord() {
        String word = "magic";
        Random random = new Random();
        if (currentRoom.getLongDescription().contains(word)) {
            List<Room> allRooms = getRooms();
            Room randomRoom = allRooms.get(random.nextInt(allRooms.size()));
            currentRoom = randomRoom;
            
            // display a complete description of the random room (exits, characters, items, movements of mobile characters)
            System.out.println("• You've been transported !" + "\n" + "\n"+ randomRoom.getLongDescription());
            System.out.println();
            System.out.println(randomRoom.getCharactersRoom());
            System.out.println("Movements of mobile characters :");
            moveCharacterRandomly(chef);
            moveCharacterRandomly(janitor);
            moveCharacterRandomly(shopkeeper);
            System.out.println();
        }
    }
    
    /**
     * Checks if the player has met all the conditions to win.
     */
    private void checkVictory() {
        boolean hasGlasses = false;
        boolean hasCane = false;
        boolean hasBaby = false;
        
        // Check if the glasses, the cane, and the baby have been given correctly and if the player is in the garden
        for (Item item : givenItems) {
            if (item.getItemName().equals("glasses")) {
                hasGlasses = true;
            } else if (item.getItemName().equals("cane")) {
                hasCane = true;
            } else if (item.getItemName().equals("baby")) {
                hasBaby = true;
            }
        }
        
        if (hasGlasses && hasCane && hasBaby && currentRoom.getLongDescription().contains("garden")) {
            System.out.println("Congratulations! You have won the game !");
            System.out.println("You can write 'quit'.");
        }
    }
    
    /**
     * Bonus task performed with the help of AI.
     * Display the map of the hotel to help the player. 
     */
    private void displayMap() {
        // Path to the JPEG image 
        String imagePath = "images/hotelmap.jpg"; 

        // Creating a Swing window to display the image
        JFrame frame = new JFrame("Moonlit Marrakech Hotel map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating a label to display the image
        JLabel label = new JLabel();
        try {
            // Loading the image using BlueJ's methods
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (inputStream != null) {
                Image img = ImageIO.read(inputStream);
                ImageIcon imageIcon = new ImageIcon(img);

                Image newImage = imageIcon.getImage().getScaledInstance(700, 500, Image.SCALE_SMOOTH);
                ImageIcon newImageIcon = new ImageIcon(newImage);

                label.setIcon(newImageIcon);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.getContentPane().add(label, BorderLayout.EAST);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
