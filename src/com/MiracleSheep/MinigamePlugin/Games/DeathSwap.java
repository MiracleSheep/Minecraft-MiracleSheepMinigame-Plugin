/**
 * Description: This is a manager class for the Death Swap minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.ObjectTypes.BlockHuntPlayer;
import com.MiracleSheep.MinigamePlugin.ObjectTypes.DeathSwapPlayer;
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
public class DeathSwap extends GameManager {

    //integers for the timer
    public static int time;
    public static int taskID2;

    // This variable will be used to set the fulltime
    public static int fulltime = 0;

    public static ArrayList<DeathSwapPlayer> playerlist = new ArrayList<DeathSwapPlayer>();

    //This variable will be used to ccheck the round number
    public static int round_num = 0;

    //This variable will be used to check if speed mode has been enabled.
    public static boolean speed = false;

    // this variable will be used to check whether all the players in the game should be teleported
    public static boolean teleport = false;

    public static boolean keepinventory = false;

    public static int lives = 0;



    //passing the instance of the main class
    public DeathSwap(MinigamePlugin main) {
        super(main);
    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {


        stopTimer(false);
        // This variable will be used to set the fulltime
         fulltime = 0;

        //This variable will be used to ccheck the round number
        round_num = 0;

        //This variable will be used to check if speed mode has been enabled.
        speed = false;

        // this variable will be used to check whether all the players in the game should be teleported
        teleport = false;

        //this variable determines whether the players will keep their inventory on death
        keepinventory = false;

        //this variable contains the lives of each player
        lives = 0;
    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
        setGame(3);
        players.add(getStartPlayer());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + "" + ChatColor.GOLD + " has started a game of " + getName() + "!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");

    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Death Swap has begun!");
        for (int i = 0 ; i < players.size() ; i++) {
            players.get(i).setHealth(20);
            players.get(i).setFoodLevel(20);
        }

        playerlist = new ArrayList<DeathSwapPlayer>();
        for (int i = 0 ; i < players.size() ; i++) {
            playerlist.add(new DeathSwapPlayer(players.get(i),lives));
        }


        if (teleport == true) {
            distribute();
        }
        setState(GameState.ACTIVE);

    }

    //function that says what do to when a player needs to be removed from the game
    @Override
    public void removeplayer(Player player) {
        players.remove(player);
    }

    //method for when a player gets eliminated
    public void playerElim(DeathSwapPlayer player) {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]:" + player.player.getDisplayName() + "" + ChatColor.GOLD + " lost a life!");
        player.lives -= 1;

        if (player.lives <= 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have no lives left and are eliminated!");
            playerlist.remove(player);
            players.remove(player.player);
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have " + player.lives + " lives left!");
        }

    }

    //function that gets called when the state is active
    @Override
    public void onActive() {
        run();

    }



    //function that gets called when the state is transition
    @Override
    public void onTransition() {
        time = 30;
        setState(GameState.ACTIVE);
    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }

    //iswon method ovveride
    @Override
    public void isWon() {


        if (players.size() == 1 || playerlist.size() == 1) {

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + "" + ChatColor.GOLD + " Wins the game!");
            setState(GameState.INACTIVE);

        } else if (players.size() == 0 || playerlist.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Everybody was eliminated this round!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Nobody wins!");
            setState(GameState.INACTIVE);
        }



    }





    public void run() {

        setTimer(main.getConfig().getInt("DeathSwapTimer"));

        startTimer();



    }


    public void startTimer() {



        int fulltime = time;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID2 = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {

                if (time == fulltime || time  == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "There will be " + time + " seconds before the swap!");
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
                }

                time = time - 1;

            }
        }, 0L, 20L);



    }

    public void setTimer(int amount) {

        if (round_num == 0) {
            time = amount*2;
        } else {

            if (speed == true) {

                if (amount - 30*round_num <= 0) {
                    time = 30;
                } else {
                    time = amount - 30*round_num;
                }
            } else {
                time = amount;
            }

        }

    }


    //This function is used to distribute all players across the world by a set distance
    public void distribute() {
        if (players.size() < 2) {
            // can't spread with zero or one players
            return;
        }

        //shuffle players
        Collections.shuffle(players);

        //getting the spawn point
        Location spawn = Bukkit.getWorld("world").getSpawnLocation();

        for (int i = 0; i < players.size(); i++) {
            spawn.add(1000,0,1000);
            spawn.setY(players.get(i).getWorld().getHighestBlockAt( players.get(i).getLocation().getBlockX(), players.get(i).getLocation().getBlockZ()).getY() + 1);
            players.get(i).teleport(spawn);
        }

    }


    //This function swaps all the players
    public void swap() {


        if (players.size() < 2) {
            // can't swap with zero or one players
            return;
        }

        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i += 2) {
            if ((players.size() - i) == 3) {
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
                i = players.size();


            } else {

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

    //method to stop the timer
    public void stopTimer(boolean restart) {
        Bukkit.getScheduler().cancelTask(taskID2);
        if (restart) {
            setState(GameState.TRANSITION);
        } else {
            round_num += 1;

            for (int i = 0 ; i < players.size() ; i++) {
                players.get(i).setHealth(20);
                players.get(i).setFoodLevel(20);
            }
        }
    }


}
