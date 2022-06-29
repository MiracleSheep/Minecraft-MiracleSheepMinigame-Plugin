/**
 * Description: This is the manager class blockhunr
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.Tasks.BlockHuntPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

//this is the manager class
public class BlockHunt extends GameManager {

    //This is an array that will hold the easy blocks
    public static String[] easyBlocks;

    //This is an array that will hold the medium blocks
    public static String[] mediumBlocks;

    //This is an array that will hold the hard blocks
    public static String[] hardBlocks;

    //This is the difficulty
    public int difficulty;

    //arraylist that holds players and their blocks
    public static ArrayList<BlockHuntPlayer> playerlist = new ArrayList<BlockHuntPlayer>();

    //this is the current difficulty
    public static int currentDifficulty = 0;

    //this will hold how far the system is
    public static int howfar = 1;

    //integers for the timer
    int time;
    public static int taskID;


    //passing the instance of the main class
    public BlockHunt(MinigamePlugin main) {
        super(main);

    }

    //function that says what do to when a player needs to be removed from the game
    @Override
    public void removeplayer(Player player) {
        players.remove(player);
    }

    //method for when a player gets eliminated
    @Override
    public void playerElim(Player player) {
        players.remove(player);
    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {
        stopTimer(false);
        reset();
    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
                setGame(1);
                players.add(getStartPlayer());
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + "" + ChatColor.GOLD + " has started a game of " + getName() + "!");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: To play, do /join!");

    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {
         start();

         for (int i = 0 ; i < players.size() ; i++) {
             players.get(i).setHealth(20);
             players.get(i).setFoodLevel(20);
         }
         Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Block Hunt has begun!");
         setState(GameState.ACTIVE);


    }

    //function that gets called when the state is active
    @Override
    public void onActive() {
        run();



    }

    //function that gets called when the state is active
    @Override
    public void onTransition() {

        setState(GameState.ACTIVE);

    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }


    //function that does the roll
    public void run() {

        playerlist = new ArrayList<BlockHuntPlayer>();

        //making a loop to give each player a block
        for (int i = 0 ; i < players.size() ; i++) {
            if (currentDifficulty == 0) {
                playerlist.add(new BlockHuntPlayer(players.get(i),easyBlocks[generaterandom()]));
            } else if (currentDifficulty == 1) {
                playerlist.add(new BlockHuntPlayer(players.get(i),mediumBlocks[generaterandom()]));
            } else if (currentDifficulty == 2) {
                playerlist.add(new BlockHuntPlayer(players.get(i), hardBlocks[generaterandom()]));
            }

            playerlist.get(i).player.sendMessage(ChatColor.AQUA + "Your assigned block is: " + playerlist.get(i).block);

        }


        setTimer(main.getConfig().getInt("BlockHuntTime"));
        startTimer();

        //checking if the difficulty is ready to be moved up
        if (howfar == difficulty) {
            if (currentDifficulty == 2) {
                howfar = 1;
            } else {
                currentDifficulty += 1;
                howfar = 1;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The difficulty has increased!");
            }
        } else {

            if (getGameState() != GameState.INACTIVE) {
                howfar += 1;
            }
        }
    }

    //method that gets random number between one and an array length
    public int generaterandom() {

        int max = 0;

        int min = 0;

        if (currentDifficulty == 0) {
            max = easyBlocks.length;
        } else if (currentDifficulty == 1) {
            max = mediumBlocks.length;
        } else if (currentDifficulty == 2) {
            max = hardBlocks.length;
        }


        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;

    }

    public void setTimer(int amount) {
        time = amount;
    }

    public void startTimer() {


        int fulltime = time;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {



//Checking if the players are standing on their blocks
                for (int i = 0; i < playerlist.size(); i++) {

                    if (playerlist.get(i).player.isOnline() && players.contains(playerlist.get(i).player)) {

                        if (playerlist.get(i).player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.getMaterial(playerlist.get(i).block))) {
                            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + playerlist.get(i).player.getDisplayName() + "" + ChatColor.GOLD + " has found their block!");
                            playerlist.remove(i);
                        } else {
                        }
                    } else {
                        playerlist.remove(i);
                    }
                }

                if (main.getConfig().getBoolean("PlayersCanFinishTimer")) {
                    if (playerlist.size() < 1 && (players.size() > 1)) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All players have found their blocks!");
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The next round will begin!");
                        stopTimer(true);
                    }

                }



                if (time == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "30 seconds remaining!");
                }

                if (time < 11) {
                    if (time == 10 && time > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Timer ends in " + (time) + "...");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + (time) + "...");
                    }

                }

                if(time == fulltime / 2) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + time + " seconds remaining!");
                }

                if(time == 0) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Time is up!");

                    for (int j = 0 ; j < playerlist.size() ; j++) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "" + playerlist.get(j).player.getDisplayName() + "" + ChatColor.GOLD + " failed to find the block " + playerlist.get(j).block + " in time!");
                        playerElim(playerlist.get(j).player);
                    }
                        isWon();
                    return;
                }



                time = time - 1;

            }
        }, 0L, 20L);



    }


    //method to stop the timer
    public void stopTimer(boolean restart) {
        Bukkit.getScheduler().cancelTask(taskID);

        if (restart) {
            setState(GameState.STARTING);
        } else {
            for (int i = 0 ; i < players.size() ; i++) {
                players.get(i).setHealth(20);
                players.get(i).setFoodLevel(20);
            }
        }
    }

    //method to reset the game
    public void reset() {
        currentDifficulty = 0;
        howfar = 1;
    }

    //iswon method ovveride
    @Override
    public void isWon() {


        if (players.size() == 1) {

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + "" + ChatColor.GOLD + " Wins the game!");
            setState(GameState.INACTIVE);

        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Everybody was eliminated this round!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Nobody wins!");
            setState(GameState.INACTIVE);
        }



    }


    //method to set starting variables
    public void start() {


        easyBlocks = new String[main.getConfig().getStringList("BlocksRolled" + ".Easy").size()];
        mediumBlocks = new String[main.getConfig().getStringList("BlocksRolled" + ".Medium").size()];
        hardBlocks = new String[main.getConfig().getStringList("BlocksRolled" + ".Hard").size()];

        for (int i = 0; i < main.getConfig().getStringList("BlocksRolled" + ".Easy").size(); i++) {
            easyBlocks[i] = main.getConfig().getStringList("BlocksRolled" + ".Easy").get(i);
        }

        for (int i = 0; i < main.getConfig().getStringList("BlocksRolled" + ".Medium").size(); i++) {
            mediumBlocks[i] = main.getConfig().getStringList("BlocksRolled" + ".Medium").get(i);
        }

        for (int i = 0; i < main.getConfig().getStringList("BlocksRolled" + ".Hard").size(); i++) {
            hardBlocks[i] = main.getConfig().getStringList("BlocksRolled" + ".Hard").get(i);
        }

        difficulty = main.getConfig().getInt("BlockHuntDifficulty");

    }


















}
