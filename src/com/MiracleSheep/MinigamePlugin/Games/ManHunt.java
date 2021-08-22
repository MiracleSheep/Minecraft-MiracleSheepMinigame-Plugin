/**
 * Description: This is a manager class for the Man Hunt minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.Items.ItemManager;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.Tasks.BlockHuntPlayer;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;


//this is the manager class
public class ManHunt extends GameManager {


    //arraylist that holds players and their blocks
    public static ArrayList<BlockHuntPlayer> playerlist = new ArrayList<BlockHuntPlayer>();

    //integers for the timer
    int time;
    public static int taskID3;

    //player that will be the runner
    public static Player runner;

    public ItemManager inv = new ItemManager(main);

    //boolean that holds whether or not the game has begun
    public static boolean started = false;

    //passing the instance of the main class
    public ManHunt(MinigamePlugin main) {
        super(main);
    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {
        stopTimer();
        started = false;
        setGame(0);
        for (int i = 0 ; i < players.size() ; i++) {
            for (int j = 0; j < players.get(i).getInventory().getSize() ; j++) {
                ItemStack item = players.get(i).getInventory().getItem(j);
                if (item != null) {


                if (item.getItemMeta().hasCustomModelData()) {
                if (item.getItemMeta().getCustomModelData() == main.i.tracker.getItemMeta().getCustomModelData()) {
                    players.get(i).getInventory().remove(item);
                }
                }
                }
            }
        }
    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
        setGame(2);
        players.add(getStartPlayer());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + "" + ChatColor.GOLD + " has started a game of " + getName() + "!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");
    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {
        runner = players.get(generaterandom());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + runner.getDisplayName() + "" + ChatColor.GOLD + " is the runner!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The grace period has started!");
        for (int i = 0 ; i < players.size() ; i++) {
            players.get(i).setHealth(20);
            players.get(i).setFoodLevel(20);
            inv.createTracker();
            if (players.get(i) != runner) {
                Location loc = players.get(i).getLocation();
                loc.setY(players.get(i).getWorld().getHighestBlockAt( players.get(i).getLocation().getBlockX(), players.get(i).getLocation().getBlockZ()).getY() + 1);
                players.get(i).teleport(loc);
                players.get(i).getInventory().addItem(inv.tracker);
            }

        }

        setState(GameState.ACTIVE);

    }

    //function that gets called when the state is active
    @Override
    public void onActive() {
        run();

    }

    //function that gets called when the state is transition
    @Override
    public void onTransition() {

    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }


    public void run() {

        setTimer(main.getConfig().getInt("ManhuntGraceTimer"));
        startTimer();

    }


    public void setTimer(int amount) {
        time = amount;
    }

    public void startTimer() {


        int fulltime = time;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID3 = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {

                if (time < 11) {
                    if (time == 10 && time > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Grace period ends in " + (time) + "...");
                    } else if (time > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + (time) + "...");
                    }

                }

                if (time == 0) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Happy hunting!");
                    started = true;
                    stopTimer();

                }

                time = time - 1;

            }
        }, 0L, 20L);



    }


    //method to stop the timer
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID3);
    }


    //method that gets random number between one and an array length
    public int generaterandom() {

        int max = players.size() - 1;

        int min = 0;


        int range = (max - min) + 1;
        return (int)((Math.random() * range) + min);

    }

















}
