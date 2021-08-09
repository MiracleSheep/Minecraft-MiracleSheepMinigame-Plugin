/**
 * Description: This is the class for the events of the game. This class listens for events and calls code when they are triggered
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */



//Name of the package
package com.MiracleSheep.MinigamePlugin.Events;

//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.Inventory.PluginInventory;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

//this is the class that listens for events
public class PluginEvents implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;
    //getting an instance of the inventory class (optionnal)
    public static PluginInventory inventory;



    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public PluginEvents(MinigamePlugin main, PluginInventory m) {
        this.main = main;
        this.inventory = m;

    }


    //This is an example of an event. When a player right clicks this function will be called. More events can be found on the spigot webpage
    @EventHandler
    public static void onPlayerRightClick(PlayerInteractEvent e) {


    }

    //This is an example of an event. When a player clicks in an inventory this function will be called. More events can be found on the spigot webpage
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        //This checks if the click was in an inventory
        if (e.getClickedInventory() == null) {
            return;
        }


        }


    }


