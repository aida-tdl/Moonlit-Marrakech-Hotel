
/**
 * Class Item - an item in an adventure game.
 * 
 * This class is part of the "Moonlit Marrakech Hotel" application.
 * 
 * An item is an object that can be found in the rooms of the Game.
 * Each item has a name and a weight.
 * Some items can be picked up, dropped and given.
 * 
 * @author Aïda Tadlaoui
 * @version 2023.12.06
 */

public class Item
{
    private String itemName;
    private int weight;
    private boolean carried;
    private boolean dropped;
    
    /**
     * Constructor for objects of class Item
     */
    public Item(String itemName, int weight)
    {
        this.itemName = itemName;
        this.weight = weight;
        this.carried = false;
        this.dropped = false;
    }
    
    /**
    * Return the name of an item.
    */
    public String getItemName(){
        return itemName;
    }
    
    /**
    * Return the weight of an item.
    */
    public int getWeight(){
        return weight;
    }
    
    /**
    * Checks if the item is currently being carried.
    * @return true if the item is being carried.
    */
    public boolean isCarried(){
        return carried;
    }
    
    /**
    * Sets the status of whether the item is being carried.
    * @param carried true if the item is to be carried.
    */
    public void setCarried(boolean carried){
        this.carried = carried;
    }
    
    /**
    * Checks if the item is currently dropped.
    * @return true if the item is dropped.
    */
    public boolean isDropped(){
        return dropped;
    }
    
    /**
    * Sets the status of whether the item is dropped.
    * @param dropped true if the item is to be dropped.
    */
    public void setDropped(boolean dropped){
        this.dropped = dropped;
    }
}
