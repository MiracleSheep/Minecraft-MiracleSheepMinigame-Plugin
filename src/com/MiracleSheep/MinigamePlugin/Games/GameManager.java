/**
 * Description: This is the lcass that manages all the minigames
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

package com.MiracleSheep.MinigamePlugin.Games;

import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameManager {

    //Getting an instance of the main class
    private final MinigamePlugin main;

    //creating a list of players
    public static ArrayList<Player> players = new ArrayList<Player>();

    //setting the default game state to inactive
    private static GameState gameState = GameState.INACTIVE;

    //This is a integer that holds the the game being played
    private static int Game = 0;

    //creating a player that is the host
    private static Player startPlayer;

    //This holds the name of the game
    private static String gameName = "None";

    //constructor
    public GameManager(MinigamePlugin main) {
        this.main = main;
    }

    //getmain function
    public MinigamePlugin getMain() {
        return this.main;
    }


    //This method controls the gamestates of the minigames
    public void setState(GameState gameState) {
        if (getGameState() == GameState.ACTIVE && gameState == GameState.STARTING) {return;}


        setGameState(gameState);

        switch (gameState) {
            case INACTIVE:
                onInactive();
                cleanup();
                break;
            case WAITING:
                onWaiting();
                break;
            case STARTING:
                onStarting();
                break;
            case ACTIVE:
                onActive();
                break;
            case WON:
                onWon();
                break;
        }

    }

    //function that sets game
    public void setGame(int intGame) {

        if (intGame == 0) {
            setName("None");
        } else if (intGame == 1) {
            setName("Block Hunt");
        } else if (intGame == 2) {
            setName("Man Hunt");
        } else if (intGame == 3) {
            setName("Man Swap");
        }
        Game = intGame;

    }


    //function that gets called when the state is inactive - works as a unique clanup functiuon
    public void onInactive() {

    }

    //function that gets called when the state is waiting
    public void onWaiting() {

    }

    //function that gets called when the state is starting
    public void onStarting() {

    }

    //function that gets called when the state is active
    public void onActive() {

    }

    //function that gets called when the state is won
    public void onWon() {

    }

    //cleanup function which cleans up variables that are the same between al games
    public void cleanup() {
        players = new ArrayList<Player>();
        setGame(0);
        setStartPlayer(null);
    }

    //method to add a player to the array and record how many players there are
    public void addPlayer(Player player) {

        players.add(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " Has joined " + getName() + "!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants ready to play!");
        if (players.size() == 2) {
            startPlayer.sendMessage(ChatColor.AQUA + "The game is ready to start!");
            startPlayer.sendMessage(ChatColor.AQUA + "Use the command /start to start " + getName() + " when you are ready!");
        }

    }

    //method to add a player to the array and record how many players tehre are
    public void playerQuit(Player player) {


        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " has quit " + getName() + "!");

        if (player == startPlayer && players.size() != 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The host has left the minigame, so " + players.get(0).getDisplayName() + " is the new host!");
            setStartPlayer(players.get(0));

        }

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants playing " + getName() + "!");

        if (players.size() == 1) {

            if (gameState == GameState.ACTIVE) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play " + getName() + "!");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
                setState(GameState.INACTIVE);
            } else if (gameState == GameState.STARTING) {
                startPlayer.sendMessage(ChatColor.AQUA + "There are not enough players to start " + getName() + "!");
                startPlayer.sendMessage(ChatColor.AQUA + "Use the command /cancel to cancel the minigame.");
            }

        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play " + getName() + "!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
            setState(GameState.INACTIVE);
        }

    }

    //method for when a player disconnects
    public void playerDisc(Player player) {

        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " has disconnected from " + getName() + "!");

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are now " + players.size() + " participants playing " + getName() + "!");

        if (player == startPlayer && players.size() != 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The host has disconnected, so " + players.get(0).getDisplayName() + " is the new host!");
            startPlayer = players.get(0);

        }

        if (players.size() == 1) {

            if (gameState == GameState.ACTIVE) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There is only one player remaining");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + " Wins the game!");
                setState(GameState.INACTIVE);
            } else if (gameState == GameState.WAITING) {
                startPlayer.sendMessage(ChatColor.AQUA + "There are not enough players to start " + getName() + "!");
                startPlayer.sendMessage(ChatColor.AQUA + "Use the command /cancel to cancel the minigame.");
            }



        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There are no longer enough players left to play " + getName() + "!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The game has been canceled!");
            setState(GameState.INACTIVE);
        }

    }

    //method for when a player gets eliminated
    public void playerElim(Player player) {

        players.remove(player);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has been eliminated from " + getName() + "!");

        if (players.size() == 1) {

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There is only one player remaining");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + " Wins the game!");
            setState(GameState.INACTIVE);
        }

    }


    //function that gets game
    public int getGame() {
        return Game;
    }


    //function that gets name of the game
    public String getName() {return gameName;}

    //function that sets name of the game
    public void setName(String name) {
        gameName = name;
    }

    //function that gets game state
    public GameState getGameState() {return gameState;}

    //function that gets game state
    public void setGameState(GameState gamestate1) {gameState = gamestate1;}

    //set method for start player
    public void setStartPlayer(Player player) {
        startPlayer = player;
    }

    //get method for startplayer
    public Player getStartPlayer() {
        return startPlayer;
    }

}
