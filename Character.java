import java.util.Random;

/**
 * 
 * Classe Character - a character in an adventure game.
 * 
 * A character has a name, a current room and represents a person.
 * Some characters can move around by themselves in the hotel.
 * 
 * @author Aïda Tadlaoui
 * @version 2023.12.06
 */

public class Character
{
    private Random random;
    private String characterName;
    private Room currentRoom;

    /**
     * Constructor for objects of class Character
     */
    public Character(String characterName)
    {
        this.characterName = characterName;
    }

    /**
     * Retrieves the name of the character.
     * @return the name of the character.
     */
    public String getCharacterName()
    {
       return characterName;
    }
    
    /**
    * Retrieves the current room where the character is located.
    * @return the room where the character is currently situated
    */
    public Room getCurrentRoom(){
        return currentRoom;
    }
    
    /**
    * Sets the current room of the character.
    * @param room the room to set as the character's current room.
    */
    public void setCurrentRoom(Room room){
        this.currentRoom = room;
    }
}
    
    
