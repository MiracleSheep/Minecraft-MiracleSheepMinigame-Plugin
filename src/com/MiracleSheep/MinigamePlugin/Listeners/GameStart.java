/**
 * Description: This class is responsable for listening to events that start the game
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */



//Name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.Games.BlockHuntManager;
import com.MiracleSheep.MinigamePlugin.Games.BlockHuntState;
import com.MiracleSheep.MinigamePlugin.Inventory.MainMenu;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

//this is the class that listens for events
public class GameStart implements Listener {

    //getting an instance of the main class
    public MinigamePlugin main;
    //getting an instance of the main inventory class
    public static MainMenu inventory;

    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public GameStart(MinigamePlugin main, MainMenu m) {
        this.main = main;
        this.inventory = m;

    }


    //This is an example of an event. When a player clicks in an inventory this function will be called. More events can be found on the spigot webpage
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        BlockHuntManager blockHuntManager = new BlockHuntManager(main);

        //This checks if the click was in an inventory
        if (e.getClickedInventory() == null) { return; }
        //Checking if clicked in the selection screen
        if (e.getClickedInventory().getHolder() instanceof MainMenu) {
            e.setCancelled(true);
        }
        Player player = (Player) e.getWhoClicked();

        if (main.getGame() != 0) {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "There is currently an ongoing game.");
            player.sendMessage(ChatColor.GREEN + "Please wait for it to finish before starting a new one.");

        } else {


        //Checking what the player clicked on
        if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.RED + "This is not an option");
            player.closeInventory();
        } else if (e.getCurrentItem().getType() == Material.DIAMOND_BLOCK) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Selecting Block Hunt!");
            player.sendMessage(ChatColor.GREEN + "Searching for players...");
            blockHuntManager.setStartPlayer(player);
            blockHuntManager.setState(BlockHuntState.WAITING);
            //Calling the block hunt class to start the game
            player.closeInventory();

        }  else if (e.getCurrentItem().getType() == Material.COMPASS) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Selecting Manhunt!");
            player.closeInventory();
        } else if (e.getCurrentItem().getType() == Material.LAVA_BUCKET) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Selecting Man Swap!");
            player.closeInventory();
        }  else if (e.getCurrentItem().getType() == Material.BARRIER) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Closed menu.");
            player.closeInventory();
        }

        }

        }


    }


