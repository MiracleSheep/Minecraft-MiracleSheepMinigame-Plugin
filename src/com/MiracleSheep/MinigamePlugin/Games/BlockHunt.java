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
import com.MiracleSheep.MinigamePlugin.ObjectTypes.BlockHuntPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    //variable that holds the number of lives
    public static int lives = 0;

    //arraylist that holds players and their blocks
    public static ArrayList<BlockHuntPlayer> playerlist = new ArrayList<BlockHuntPlayer>();

    public static ArrayList<Player> deadfolk = new ArrayList<Player>();

    //this is the current difficulty
    public static int currentDifficulty = 0;

    //this will hold how far the system is
    public static int howfar = 1;


    //This variable will be used to ccheck the round number
    public static int round_num = 0;

    //This variable will be used to check if speed mode has been enabled.
    public static boolean speed = false;

    //this variable determines whether it is the first time starting or not
    public static boolean first_start = true;

    // this variable will be used to check whether all the players in the game should be teleported
    public static boolean teleport = false;

    public static boolean keepinventory = false;

    public static boolean sameblock = false;





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

        if (player == startPlayer && players.size() != 0) {
            players.get(0).sendMessage(ChatColor.AQUA + "You are the new game host!");
            setStartPlayer(players.get(0));

        }

    }

    //method for when a player gets eliminated
    public void playerElim(BlockHuntPlayer player) {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]:" + player.player.getDisplayName() + "" + ChatColor.GOLD + " failed to find the block " + player.block + " in time!");
        playerlist.get(playerlist.indexOf(player)).lives -= 1;

        if (playerlist.get(playerlist.indexOf(player)).lives <= 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have no lives left and are eliminated!");
            playerlist.remove(player);
            removeplayer(player.player);
            deadfolk.add(player.player);
            player.player.setGameMode(GameMode.SPECTATOR);
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have " + player.lives + " lives left!");
        }

    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {

        for (int i = 0 ; i < players.size() ; i++) {
            players.get(i).setGameMode(GameMode.SURVIVAL);
            players.get(i).setHealth(20);
            players.get(i).setFoodLevel(20);
            players.get(i).setSaturation(20);
        }

        for (int o = 0 ; o  < players.size() ; o++) {
            if (!(players.get(o).isOnline())) {
                players.remove(players.get(0));
            } else {
                players.get(o).teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
        }

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

        if (first_start) {

            for (int i = 0 ; i < players.size() ; i++) {
                players.get(i).setHealth(20);
                players.get(i).setFoodLevel(20);
            }

            playerlist = new ArrayList<BlockHuntPlayer>();
            for (int i = 0 ; i < players.size() ; i++)  {
                playerlist.add(new BlockHuntPlayer(players.get(i), "",lives));
            }

            if (teleport == true) {
                distribute();
            }

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Block Hunt has begun!");

            first_start = false;
        }

        damage = true;
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



        if (sameblock == false) {
        //making a loop to give each player a block
        for (int i = 0 ; i < players.size() ; i++) {
            if (currentDifficulty == 0) {
                playerlist.get(i).block = easyBlocks[generaterandom()];
            } else if (currentDifficulty == 1) {
                playerlist.get(i).block = mediumBlocks[generaterandom()];
            } else if (currentDifficulty == 2) {
                playerlist.get(i).block = hardBlocks[generaterandom()];
            }

        if (playerlist.get(i).player.isOnline()) {
            playerlist.get(i).player.sendMessage(ChatColor.AQUA + "Your assigned block is: " + playerlist.get(i).block);
        }


        }
        } else {
            int selectedblock = generaterandom();

            if (currentDifficulty == 0) {
                for (int i = 0 ; i < players.size() ; i++) {
                    playerlist.get(i).block = easyBlocks[selectedblock];
                    if (playerlist.get(i).player.isOnline()) {
                        playerlist.get(i).player.sendMessage(ChatColor.AQUA + "Your assigned block is: " + playerlist.get(i).block);
                    }
                }
            } else if (currentDifficulty == 1) {
                for (int i = 0 ; i < players.size() ; i++) {
                    playerlist.get(i).block = mediumBlocks[selectedblock];
                    if (playerlist.get(i).player.isOnline()) {
                        playerlist.get(i).player.sendMessage(ChatColor.AQUA + "Your assigned block is: " + playerlist.get(i).block);
                    }
                }
            } else if (currentDifficulty == 2) {
                    for (int i = 0 ; i < players.size() ; i++) {
                        playerlist.get(i).block = hardBlocks[selectedblock];
                        if (playerlist.get(i).player.isOnline()) {
                            playerlist.get(i).player.sendMessage(ChatColor.AQUA + "Your assigned block is: " + playerlist.get(i).block);
                        }
                }

            }
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

    //This function is used to distribute all players across the world by a set distance
    public void distribute() {
        damage = false;
        if (playerlist.size() < 2) {
            // can't spread with zero or one players
            return;
        }

        //shuffle players
        Collections.shuffle(playerlist);

        for (int i = 0; i < playerlist.size(); i++) {

            playerlist.get(i).spawnpoint = Bukkit.getWorld("world").getSpawnLocation().add((1+i)*1000,0,(1+i)*1000);
            playerlist.get(i).spawnpoint.setY(355);
            playerlist.get(i).player.teleport(playerlist.get(i).spawnpoint);
            playerlist.get(i).spawnpoint.setY(playerlist.get(i).player.getWorld().getHighestBlockAt( playerlist.get(i).player.getLocation().getBlockX(), playerlist.get(i).player.getLocation().getBlockZ()).getY() + 1);
            playerlist.get(i).player.teleport(playerlist.get(i).spawnpoint);
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

            if (speed == true) {

                if (amount - 15*round_num <= 30) {
                    time = 30;
                } else {
                    time = amount - 15*round_num;
                }
            } else {
                time = amount;
            }


    }

    public void startTimer() {


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        int fulltime = time;
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "You have " + time + " seconds to find your blocks!");
        taskID = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {



//Checking if the players are standing on their blocks
                for (int i = 0; i < playerlist.size(); i++) {
                    if (players.contains(playerlist.get(i).player)) {

                        if (playerlist.get(i).player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.getMaterial(playerlist.get(i).block )) && playerlist.get(i).found == false) {
                            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + playerlist.get(i).player.getDisplayName() + "" + ChatColor.GOLD + " has found their block!");
                            playerlist.get(i).found = true;
                        } else {
                        }
                    } else {
                        playerlist.remove(i);
                    }
                }

                if (main.getConfig().getBoolean("PlayersCanFinishTimer")) {

                    //checking if all the players have found their blocks
                    boolean allfound = true;
                    for (int i = 0 ; i < playerlist.size() ; i++) {
                        if (playerlist.get(i).found == false) {
                            i = playerlist.size();
                            allfound = false;
                        }
                    }

                    if (allfound && isWon() == false) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All players have found their blocks!");
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The next round will begin!");
                        stopTimer(true);
                    }



                }



                if (time == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "You have " + time + " seconds to find your blocks!");
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
                    ArrayList<BlockHuntPlayer> removelist = new ArrayList<BlockHuntPlayer>();
                    int limit = playerlist.size();
                    for (int j = 0 ; j < limit ; j++) {
                        if (playerlist.get(j).found == false) {
                            removelist.add(playerlist.get(j));
                        } else {
                            playerlist.get(j).found = false;
                        }

                    }

                    for (int j = 0 ; j < removelist.size(); j++) {
                        playerElim(removelist.get(j));
                    }

                    boolean test = isWon();
                    if (test) {
                        stopTimer(false);
                        setState(GameState.INACTIVE);
                    } else {
                        stopTimer(true);
                    }
                }



                time = time - 1;

            }
        }, 0L, 20L);



    }


    //method to stop the timer
    public void stopTimer(boolean restart) {
        Bukkit.getScheduler().cancelTask(taskID);

        if (restart) {
            round_num += 1;
            for (int i = 0 ; i < playerlist.size() ; i++) {
                playerlist.get(i).found = false;
            }

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

        stopTimer(false);

        currentDifficulty = 0;
        howfar = 1;

        lives = 0;

        //This variable will be used to ccheck the round number
        round_num = 0;

        first_start = true;

        //This variable will be used to check if speed mode has been enabled.
        speed = false;

        // this variable will be used to check whether all the players in the game should be teleported
        teleport = false;

        keepinventory = false;

        sameblock = false;

        deadfolk.clear();
    }

    //iswon method ovveride
    @Override
    public boolean isWon() {


        if (playerlist.size() == 1) {

            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + players.get(0).getDisplayName() + "" + ChatColor.GOLD + " Wins the game!");
            return true;

        } else if (playerlist.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Everybody was eliminated this round!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Nobody wins!");
            return true;
        }

        return false;
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


    }


















}
