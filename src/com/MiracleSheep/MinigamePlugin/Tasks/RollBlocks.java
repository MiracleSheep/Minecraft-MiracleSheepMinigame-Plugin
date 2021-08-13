/**
 * Description: This class reruns the block roll and timer whenever it is called
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

package com.MiracleSheep.MinigamePlugin.Tasks;

import com.MiracleSheep.MinigamePlugin.Games.BlockHunt;
import com.MiracleSheep.MinigamePlugin.Games.GameState;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

//roll class
public class RollBlocks {

    //creating a block hunt object
    public BlockHunt blockHunt;

    //creating an instance of the main plugin
    public MinigamePlugin main;

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
    public static int howfar = 0;

    //integers for the timer
    int time;
    int taskID;

    //Constructor
   RollBlocks(MinigamePlugin main, int difficulty, String[] easyblocks, String[] mediumblocks, String[] hardblocks) {

       this.main = main;
       this.difficulty = difficulty;
       easyBlocks = easyblocks;
       mediumBlocks = mediumblocks;
       hardBlocks = hardblocks;


   }

   //function that does the roll
   public void run() {

       playerlist = new ArrayList<BlockHuntPlayer>();

        //making a loop to give each player a block
       for (int i = 0 ; i < blockHunt.players.size() ; i++) {
           if (currentDifficulty == 0) {
               playerlist.add(new BlockHuntPlayer(blockHunt.players.get(i),easyBlocks[generaterandom()]));
           } else if (currentDifficulty == 1) {
               playerlist.add(new BlockHuntPlayer(blockHunt.players.get(i),mediumBlocks[generaterandom()]));
           } else if (currentDifficulty == 2) {
               playerlist.add(new BlockHuntPlayer(blockHunt.players.get(i), hardBlocks[generaterandom()]));
           }

       }


       setTimer(main.getConfig().getInt("BlockHuntTime"));
       startTimer();

       //checking if the difficulty is ready to be moved up
       if (howfar == difficulty) {
           if (currentDifficulty == 3) {
               howfar = 0;
           } else {
               currentDifficulty += 1;
               howfar = 0;
               Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The difficulty has increased!");
           }
       } else {

           if (blockHunt.getGameState() != GameState.INACTIVE) {
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

                    if (playerlist.get(i).player.isOnline()) {

                    if (playerlist.get(i).player.getLocation().getBlock().getType() == Material.getMaterial(playerlist.get(i).block)) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + playerlist.get(i).player.getDisplayName() + " has found their block!");
                        playerlist.remove(i);
                    }
                    } else {
                        playerlist.remove(i);
                    }
                }

                if (main.getConfig().getBoolean("PlayersCanFinishTimer")) {
                    if (playerlist.size() < 1) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All players have found their blocks!");
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The next round will begin!");
                        stopTimer();
                    }

                }

                if(time == 0) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Time is up!");
                    stopTimer();
                    return;
                }

                if (time == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "30 seconds remaining!");
                }

                if (time < 10) {
                    if (time == 9) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Timer ends in " + (time + 1) + "...");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + (time + 1) + "...");
                    }

                }

                if(time == fulltime / 2) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + time + " seconds remaining!");
                }



                time = time - 1;

            }
        }, 0L, 20L);



    }


    //method to stop the timer
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    //method to reset the game
    public void reset() {
       currentDifficulty = 0;
       howfar = 0;
    }




}
