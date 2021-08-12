/**
 * Description: This is the manager class for all of the games. The games work by being segmented into different stages and the manager is responsable for activating these stages and chaning the game state
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

//this is the manager class
public class BlockHuntManager {

    //Getting an instance of the main class
    private final MinigamePlugin main;

    //setting the default game state to inactive
    private static BlockHuntState blockHuntState = BlockHuntState.INACTIVE;

    //creating a list of players
    public static ArrayList<Player> players = new ArrayList<Player>();

    //creating a
    private static Player startPlayer;

    //passing the instance of the main class
    public BlockHuntManager(MinigamePlugin main) {
        this.main = main;
    }

    //This method controls the gamestates of the minigames
    public void setState(BlockHuntState blockHuntState) {
        if (this.blockHuntState == BlockHuntState.ACTIVE && blockHuntState == BlockHuntState.STARTING) {return;}


        this.blockHuntState = blockHuntState;

        switch (blockHuntState) {
            case INACTIVE:
                players = new ArrayList<Player>();
                main.setGame(0);
                setStartPlayer(null);
                break;
            case WAITING:
                //waiting function called
                    main.setGame(1);;
                    players.add(startPlayer);
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + startPlayer.getDisplayName() + " has started a game of Block Hunt!");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");
                break;
            case ACTIVE:
                Bukkit.broadcastMessage("Active");
                break;
            case STARTING:
                Bukkit.broadcastMessage("Starting");
                break;
        }

    }

    //method to add a player to the array and record how many players tehre are
    public void addPlayer(Player player) {

        players.add(player);
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " Has joined Block Hunt!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants ready to play!");
        if (players.size() == 2) {
            startPlayer.sendMessage(ChatColor.AQUA + "The game is ready to start!");
            startPlayer.sendMessage(ChatColor.AQUA + "Use the command /start to start the Block Hunt when you are ready!");
        }

    }


    //method to add a player to the array and record how many players tehre are
    public void playerQuit(Player player) {


        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " has quit Block Hunt!");

        if (player == startPlayer && players.size() != 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The host has left minigame, so " + players.get(0).getDisplayName() + " is the new host!");
            startPlayer = players.get(0);

        }

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants playing Block Hunt!");

        if (players.size() == 1) {

            if (blockHuntState == BlockHuntState.ACTIVE) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play Block Hunt!");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
                cleanup();
            } else if (blockHuntState == BlockHuntState.STARTING) {
                startPlayer.sendMessage(ChatColor.AQUA + "There are not enough players to start Block Hunt!");
                startPlayer.sendMessage(ChatColor.AQUA + "Use the command /cancel to cancel the minigame.");
            }

        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play Block Hunt!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
            cleanup();
        }

    }

    //method for when a player gets eliminated
    public void playerElim(Player player) {

        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has been eliminated from Block Hunt!");

        if (players.size() == 1) {

                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There is only one player remaining");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + " Wins the game!");
                cleanup();

        }

    }

    //method for when a player disconnects
    public void playerDisc(Player player) {

        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " has disconnected from Block Hunt!");

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants playing Block Hunt!");

        if (player == startPlayer && players.size() != 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The host has disconnected, so " + players.get(0).getDisplayName() + " is the new host!");
            startPlayer = players.get(0);

        }

        if (players.size() == 1) {

            if (blockHuntState == BlockHuntState.ACTIVE) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There is only one player remaining");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + " Wins the game!");
                cleanup();
            } else if (blockHuntState == BlockHuntState.WAITING) {
                startPlayer.sendMessage(ChatColor.AQUA + "There are not enough players to start Block Hunt!");
                startPlayer.sendMessage(ChatColor.AQUA + "Use the command /cancel to cancel the minigame.");
            }



        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play Block Hunt!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
            cleanup();
        }

    }

    //set method for start player
    public void setStartPlayer(Player player) {
        startPlayer = player;
    }

    //get method for startplayer
    public Player getStartPlayer() {
        return startPlayer;
    }

    public BlockHuntState getBlockHuntState() {return blockHuntState;}

    //code meant for cleaning up the game
    public void cleanup() {
        setState(BlockHuntState.INACTIVE);
    }

}
