import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Name : Aïda Tadlaoui 
 * K number : 23090130
 * 
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Moonlit Marrakech Hotel" application.   
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling, David J. Barnes and Aïda Tadlaoui
 * @version 2023.12.06
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;   // stores exits of this room.
    
    private List<Item> items;
    private List<Character> characters;
    
    private Room currentRoom;
    private Room startRoom;
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<>();
        characters = new ArrayList<>();
        initializeItems(); 
        initializeCharacters(); 
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * @return The long description of the room (exits and items)
     */
    public String getLongDescription()
    {
        String descriptionString = "• You are " + description + ".\n" + getExitString();
        
        if (!items.isEmpty()){
            descriptionString += "\n" + "\n• Items in this room:";
            for (Item item : items) {
                if (!item.isCarried() || item.isDropped()) {
                    descriptionString += "\n- " + item.getItemName();
                }
            }
        }
        return descriptionString;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
    * Retrieves the available exits from the current room.
    * @return an array of available exit names.
    */
    public String[] getAvailableExit(){
        return exits.keySet().toArray(new String[0]);
    }
    
    /**
     * Adds an item to the collection within the room.
     * @param item the item to be added to the room.
     */
    public void addItems(Item item)
    {
        items.add(item);
    }
    
    /**
     * Removes an item from the collection within the room.
     * @param item the item to be removed from the room.
     */
    public void removeItems(Item item)
    {
        items.remove(item);
    }
    
    /**
    * Create the items (name, weight) and put them in the rooms.
    */
    public void initializeItems(){
        if (this.description.equals("in the souvenir shop")){
            items.add(new Item("glasses", 30));
        } else if (this.description.equals("in the conference room")){
            items.add(new Item("baby", 3000));
        } else if (this.description.equals("in the luggage storage")){
            items.add(new Item("cane", 200));
        } else if (this.description.equals("in the library")){
            items.add(new Item("book", 300));
        } else if (this.description.equals("in the restaurant")){
            items.add(new Item("table", 10000));
        } else if (this.description.equals("in the lounge")){
            items.add(new Item("sofa", 30000));
        } else if (this.description.equals("in the garden")){
            items.add(new Item("tree", 2000000));
        }
    }
    
    /**
    * Return a list containing all the items in the game.
    * @return List of all the items.
    */
    public List<Item> getItems(){
        return items;
    }
    
    /**
    * Adds a character to the collection of characters within the room.
    * Sets the character's current room to this room.
    *  @param character the character to be added to the room.
    */
    public void addCharacters(Character character){
        characters.add(character);
        character.setCurrentRoom(this);
    }
    
    /**
    * Removes a character from the collection of characters within the room.
    * @param character the character to be removed from the room.
    */
    public void removeCharacters(Character character){
        characters.remove(character);
    }
    
    /**
     * Create the stationary characters and place them in the rooms.
     */
    public void initializeCharacters()
    {
        Character mother, oldman, person;
        
        if (this.description.equals("in the restaurant")) {
            mother = new Character("The mother");
            addCharacters(mother);
            mother.setCurrentRoom(this);
        } else if (this.description.equals("in the lounge")) {
            oldman = new Character("The old man");
            addCharacters(oldman);
            oldman.setCurrentRoom(this);
        } else if (this.description.equals("in the library")) {
            person = new Character("The visually impaired person");
            addCharacters(person);
            person.setCurrentRoom(this);
        }
    }
    
    /**
     * Displays the characters present in the current room.
     */
    public String getCharactersRoom()
    {
        StringBuilder charactersString = new StringBuilder();
        
        if (!characters.isEmpty()) {
            charactersString.append("• Characters in this room:\n");
            for(Character character : characters){
                if (character.getCurrentRoom() == this){
                    charactersString.append("- ").append(character.getCharacterName()).append("\n");
                }
            }
        }
        return charactersString.toString();
    }
}




