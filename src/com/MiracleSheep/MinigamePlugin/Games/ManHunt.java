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
import com.MiracleSheep.MinigamePlugin.ObjectTypes.ManHuntPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;


//this is the manager class
public class ManHunt extends GameManager {


    //integers for the timer

    //boolean that hold whether or not the  hunter will keep their inventory
    public static boolean hunterKeep = false;


    public static boolean runnerKeep = false;


    int time;
    public static int taskID3;

    //integer that holds the number of lives that all runners start with
    public static int lives = 0;

    //integer that holds the limit
    public static int limit = 0;

    //players that will be the runner
    public static ArrayList<ManHuntPlayer> runners = new ArrayList<ManHuntPlayer>();

    //players that will be the hunter
    public static ArrayList<Player> hunters = new ArrayList<Player>();

    //players that are dead
    public static ArrayList<Player> deadfolk = new ArrayList<Player>();

    public ItemManager inv = new ItemManager(main);

    //boolean that holds whether or not the game has begun
    public static boolean started = false;

    //passing the instance of the main class
    public ManHunt(MinigamePlugin main) {
        super(main);
    }

    @Override
    public void removeplayer(Player player) {
        players.remove(player);
        for (int i = 0 ; i < runners.size(); i++)  {
            if (runners.get(i).player == player) {
                runners.remove(i);
            }
        }
        for (int i = 0 ; i < hunters.size(); i++)  {
            if (hunters.get(i) == player) {
                hunters.remove(i);
            }
        }
        for (int i = 0 ; i < deadfolk.size(); i++)  {
            if (deadfolk.get(i) == player) {
                deadfolk.remove(i);
            }
        }
        isWon();
    }

    @Override
    public void playerElim(Player player) {

            for (int i = 0 ; i < runners.size(); i++)  {
                if (runners.get(i).player == player) {
                    runners.remove(i);
                    deadfolk.add(player);
                }
            }
            isWon();
    }

    //checks if the game is won and acts if it is
    public void isWon() {
        if (runners.size() == 0) {
            onWon(1);
        }
        if (hunters.size() == 0) {
            onWon(2);
        }

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
        limit = 0;
        lives = 0;
        players.clear();
        hunters.clear();
        deadfolk.clear();
        runners.clear();
        WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
        wb.reset();
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


        //creating the world border if that limit had been set
        if (limit == 1) {
            Location l = Bukkit.getWorld("world").getSpawnLocation();
            Location neareststronghold = Bukkit.getWorld("world").locateNearestStructure(l, StructureType.STRONGHOLD,72,true);
            WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
            wb.setCenter(l);

            double neareststronghold_x = neareststronghold.getX();
            double neareststronghold_z = neareststronghold.getZ();

            if (neareststronghold_x >= neareststronghold_z) {
                wb.setSize((neareststronghold_x*2) + main.getConfig().getInt("ManhuntBorderDistance"));
            } else if (neareststronghold_x <= neareststronghold_z) {
                wb.setSize((neareststronghold_z*2) + main.getConfig().getInt("ManhuntBorderDistance"));
            }



        }

        // putting all players not in the hunters into the runners
        for (int i = 0 ; i < players.size() ; i++) {

            if (hunters.contains(players.get(i))) {

            } else {
                runners.add(new ManHuntPlayer(players.get(i),lives));
            }

        }

        // broadcasting the runners
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Runners:");
        for (int i = 0; i < runners.size(); i++ ) {
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "- " + runners.get(i).player.getDisplayName());
        }

        // broadcasting the hunters
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Hunters:");
        for (int i = 0; i < hunters.size(); i++ ) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "- " + hunters.get(i).getDisplayName());
        }


        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The grace period has started!");
        for (int i = 0 ; i < players.size() ; i++) {
            players.get(i).setHealth(20);
            players.get(i).setFoodLevel(20);
            for (int j = 0 ; j < hunters.size(); j++)  {
                inv.createTracker(hunters.get(j));
            }
            if (hunters.contains(players.get(i))) {
                Location loc = players.get(i).getLocation();
                loc.setY(players.get(i).getWorld().getHighestBlockAt( players.get(i).getLocation().getBlockX(), players.get(i).getLocation().getBlockZ()).getY() + 1);
                players.get(i).teleport(loc);
//                players.get(i).getInventory().addItem(inv.tracker);
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
    public void onWon(int whowon) {
        if (whowon == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Ender Dragon has been defeated!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Runners have won!");
            setState(GameState.INACTIVE);
        } else if (whowon == 1) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All the runners have died!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Hunters have won!");
            setState(GameState.INACTIVE);
        } else if (whowon == 2) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All the hunters have given up!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Runners have won!");
            setState(GameState.INACTIVE);
        }
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
