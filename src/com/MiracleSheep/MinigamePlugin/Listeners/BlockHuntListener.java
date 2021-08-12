/**
 * Description: this class is responsable for listening to events from the block hunt minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//This is the name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//These are the required librairies and packages
import com.MiracleSheep.MinigamePlugin.Games.BlockHuntManager;
import com.MiracleSheep.MinigamePlugin.Inventory.MainMenu;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

//this is the class that listens for block hunt events
public class BlockHuntListener implements Listener  {

    //getting an instance of the main class
    public static MinigamePlugin main;


    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public BlockHuntListener(MinigamePlugin main) {
        this.main = main;
    }

    //This is the event that detects when somebody disconnects
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        BlockHuntManager blockHuntManager = new BlockHuntManager(main);

        //getting the player who disconnected
        Player player = (Player) e.getPlayer();

        //checking if the player is in the lobby
        if (blockHuntManager.players.contains(player)) {
            blockHuntManager.playerDisc(player);
        }

    }


}
