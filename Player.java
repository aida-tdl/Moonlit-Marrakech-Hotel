import java.util.ArrayList;
import java.util.List;

/**
 * Name : Aïda Tadlaoui 
 * K number : 23090130
 * 
 * Class Player - a player is the main character of the game.
 * 
 * This class is part of the "Moonlit Marrakech Hotel" application.
 * 
 * The player can take, drop and give items from his inventory.
 * 
 * @author Aïda Tadlaoui
 * @version 2023.12.06
 */

public class Player
{
    private int weightLimit;
    private int currentWeight;
    private List<Item> inventory;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        this.weightLimit = 3030;
        this.currentWeight = 0;
        this.inventory = new ArrayList<>();
    }
    
    /**
     * Returns a list containing all the items in the inventory.
     * @return List of all the items in the inventory.
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Checks if an item can be taken, add it to the inventory and add his weight to the current weight.
     */
    public boolean canTakeItem(Item item) {
        int itemWeight = item.getWeight();
        
        if ((currentWeight + itemWeight) <= weightLimit) {
            inventory.add(item);
            currentWeight += itemWeight;
            return true;
        }
        return false;
    }

    /**
     * Sets an item as carried.
     */
    public boolean isCarrying(String itemName) {
        for (Item item : inventory) {
            if (item.getItemName().equals(itemName) && item.isCarried()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if an item can be dropped, remove it from the inventory and remove his weight from the current weight.
     */
    public void dropItem(Item item) {
        if (inventory.contains(item)){
            inventory.remove(item);
            currentWeight -= item.getWeight();
            item.setCarried(false);
            item.setDropped(true);
            System.out.println("• You dropped the " + item.getItemName() + ".");
        }
    }
    
    /**
     * Allows to give an item, remove it from the inventory and remove his weight from the current weight.
     */
    public void giveItem(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            currentWeight -= item.getWeight();
            item.setCarried(false);
        }
    }
}
