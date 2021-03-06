/**
 * Description: This class is responsable for listening to events that start the game
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */



//Name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.Games.*;
import com.MiracleSheep.MinigamePlugin.Inventory.MainMenu;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

//this is the class that listens for events
public class GameListener implements Listener {

    //getting an instance of the main class
    public MinigamePlugin main;
    //getting an instance of the main inventory class
    public static MainMenu inventory;

    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public GameListener(MinigamePlugin main, MainMenu m) {
        this.main = main;
        this.inventory = m;

    }

    //This is the event that detects when somebody disconnects
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        BlockHunt blockhunt  = new BlockHunt(main);
        ManHunt manhunt = new ManHunt(main);
        ManSwap manswap = new ManSwap(main);
        GameManager manager = new GameManager(main);

        //getting the player who disconnected
        Player player = (Player) e.getPlayer();

        //checking if the player is in the lobby
        if (manager.players.contains(player)) {
            if (manager.getGame() == 1) {
                blockhunt.playerDisc(player);
            } else if (manager.getGame() == 2) {
                manhunt.playerDisc(player);
            } else if (manager.getGame() == 3) {
                manswap.playerDisc(player);
            }
        }

    }


    //This is an example of an event. When a player clicks in an inventory this function will be called. More events can be found on the spigot webpage
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        BlockHunt blockhunt  = new BlockHunt(main);
        ManHunt manhunt = new ManHunt(main);
        ManSwap manswap = new ManSwap(main);

        //This checks if the click was in an inventory
        if (e.getClickedInventory() == null) { return; }
        //Checking if clicked in the selection screen
        if (e.getClickedInventory().getHolder() instanceof MainMenu) {
            e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        if (manhunt.getGame() != 0) {
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
            blockhunt.setStartPlayer(player);
            blockhunt.setState(GameState.WAITING);
            //Calling the block hunt class to start the game
            player.closeInventory();

        }  else if (e.getCurrentItem().getType() == Material.COMPASS) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Selecting Manhunt!");
            player.sendMessage(ChatColor.GREEN + "Searching for players...");
            player.closeInventory();
            manhunt.setStartPlayer(player);
            manhunt.setState(GameState.WAITING);
        } else if (e.getCurrentItem().getType() == Material.LAVA_BUCKET) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Selecting Man Swap!");
            player.sendMessage(ChatColor.GREEN + "Searching for players...");
            player.closeInventory();
            manswap.setStartPlayer(player);
            manswap.setState(GameState.WAITING);
        }  else if (e.getCurrentItem().getType() == Material.BARRIER) {
            //outputting the choice to the player
            player.sendMessage(ChatColor.GREEN + "Closed menu.");
            player.closeInventory();
        }

        }

        }
    }


    }


