/**
 * Description: This is a manager class for the Man Swap minigame
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//this is the manager class
public class ManSwap extends GameManager {

    //integers for the timer
    int time;
    public static int taskID2;

    //passing the instance of the main class
    public ManSwap(MinigamePlugin main) {
        super(main);
    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {
        stopTimer(false);
    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
        setGame(3);
        players.add(getStartPlayer());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + " has started a game of " + getName() + "!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");

    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Man Swap has begun!");
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

        setState(GameState.ACTIVE);
    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }

    //iswon method ovveride
    @Override
    public void isWon() {


        if (players.size() == 1) {

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + " Wins the game!");
            setState(GameState.INACTIVE);

        } else if (players.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Everybody was eliminated this round!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Nobody wins!");
            setState(GameState.INACTIVE);
        }



    }

    //method to stop the timer
    public void stopTimer(boolean restart) {
        Bukkit.getScheduler().cancelTask(taskID2);

        if (restart) {
            setState(GameState.TRANSITION);
        }
    }



    public void run() {

        setTimer(main.getConfig().getInt("ManSwapTimer"));
        startTimer();



    }


    public void startTimer() {


        int fulltime = time;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID2 = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {


                if (time == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "30 seconds before swap!");
                }

                if (time < 11) {
                    if (time == 10) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Swapping in " + (time) + "...");
                    } else if (time > 0){
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + (time) + "...");
                    }

                }

                if(time == fulltime / 2) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + time + " seconds to swap!");
                }

                if(time == 0) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Time is up!");
                    swap();
                    stopTimer(true);

                    return;
                }



                time = time - 1;

            }
        }, 0L, 20L);



    }

    public void setTimer(int amount) {
        time = amount;
    }

    //This function swaps all the players
    public void swap() {


        if (players.size() < 2) {
            // can't swap with zero or one players
            return;
        }

        //Collections.shuffle(players);
        for (int i = 0; i < players.size(); i += 2) {
            if (players.size() - i == 3) {
                Bukkit.broadcastMessage("swap three");
                //swap the last three players instead
                Player one = players.get(i);
                Player two = players.get(i+1);
                Player three = players.get(i+2);

                Location loc = one.getLocation();
                Location loc2= two.getLocation();
                Location loc3 = three.getLocation();

                one.teleport(loc2);
                two.teleport(loc3);
                three.teleport(loc);


            } else {

                Bukkit.broadcastMessage("swap two");
                Player one = players.get(i);
                Player two = players.get(i + 1);

                Location loc = one.getLocation();
                Location loc2 = two.getLocation();

                one.teleport(loc2);
                two.teleport(loc);
                //teleport one to two, and two to one.
            }
        }
    }


}
