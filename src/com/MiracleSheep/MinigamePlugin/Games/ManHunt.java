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
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
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

    //boolean that holds the trac cking type of the compass
    public static boolean trackindividuals = false;

    //checks if the win has been activated.
    public static boolean activatedwin = false;


    public static ArrayList<ArmorStand> disconnected_players = new ArrayList<ArmorStand>();

    public static Location spawn = Bukkit.getWorld("world").getSpawnLocation();


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

        if (player == startPlayer && isWon() == false) {
            players.get(0).sendMessage(ChatColor.AQUA + "You are the new game host!");
            setStartPlayer(players.get(0));

        }
    }

    @Override
    public void playerElim(Player player) {

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The runner " + player.getDisplayName() + "" + ChatColor.GOLD + " has has lost a life!");

        runners.get(getRunner(player)).lives -= 1;

        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have " + runners.get(getRunner(player)).lives + "" + ChatColor.GOLD + " lives remaining.");

        if (runners.get(getRunner(player)).lives <= 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + " has been eliminated from ManHunt!");

            for (int i = 0 ; i < runners.size(); i++)  {
                if (runners.get(i).player == player) {
                    runners.remove(i);
                    deadfolk.add(player);
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
            isWon();

        }





    }

    //checks if the game is won and acts if it is
    public boolean isWon() {
        //checking if the game is still manhunt
        if (this.getGame() == 2 && activatedwin == false) {

            if (runners.size() == 0) {
                onWon(1);
                return true;
            } else if (hunters.size() == 0) {
                onWon(2);
                return true;
            }

        }
        return false;

    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {

        for (int o = 0 ; o  < players.size() ; o++) {
            if (!(players.get(o).isOnline())) {
                players.remove(players.get(0));
            } else {
                players.get(o).teleport(spawn);
            }
        }

        stopTimer();
        started = false;
        setGame(0);


        //This loop sets all players out of spectatormode and returns everyone to spawn
        for (int i = 0 ; i < players.size() ; i++) {
            players.get(i).setGameMode(GameMode.SURVIVAL);
            players.get(i).setHealth(20);
            players.get(i).setFoodLevel(20);
            players.get(i).setSaturation(20);
        }


        // This loop removes the compass from any player's inventory
        for (int i = 0 ; i < players.size() ; i++) {
            for (int j = 0; j < players.get(i).getInventory().getSize() ; j++) {
                ItemStack item = players.get(i).getInventory().getItem(j);
                if (item != null) {


                if (item.getItemMeta().hasCustomModelData()) {

                if (item.getItemMeta().getCustomModelData() == 123) {

                    players.get(i).getInventory().remove(item);
                }
                }
                }
            }
        }
        limit = 0;
        lives = 0;
        trackindividuals = false;
        players.clear();
        hunters.clear();
        deadfolk.clear();
        runners.clear();
        activatedwin = false;

        for (int j = 0 ; j < disconnected_players.size() ; j++) {
            disconnected_players.get(j).remove();
        }

        disconnected_players.clear();

        trackindividuals = false;
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
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Starting...");
        //creating the world border if that limit had been set
        if (limit == 1) {
            WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
            Location neareststronghold = Bukkit.getWorld("world").locateNearestStructure(spawn, StructureType.STRONGHOLD,100,false);
            Bukkit.broadcastMessage(ChatColor.GREEN + "Activating Border!");
            wb.setCenter(spawn);
            double neareststronghold_x = neareststronghold.getX();
            double neareststronghold_z = neareststronghold.getZ();


            if ( Math.abs(neareststronghold_x)  >= Math.abs(neareststronghold_z)) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "The border will have a radius of " + (int) (Math.abs(neareststronghold_x) + main.getConfig().getInt("ManhuntBorderDistance")) + " blocks.");
                wb.setSize((Math.abs(neareststronghold_x) + main.getConfig().getInt("ManhuntBorderDistance"))*2);
            } else if (Math.abs(neareststronghold_x)  <= Math.abs(neareststronghold_z)) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "The border will have a radius of " + (int) (Math.abs(neareststronghold_z) + main.getConfig().getInt("ManhuntBorderDistance")) + " blocks.");
                wb.setSize((Math.abs(neareststronghold_z) + main.getConfig().getInt("ManhuntBorderDistance"))*2);
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

        if (hunters.size() == 0) {
            isWon();
            return;
        } else if (runners.size() == 0) {
            isWon();
            return;
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
        activatedwin = true;

        if (whowon == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Ender Dragon has been defeated!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Runners have won!");
            setState(GameState.INACTIVE);
        } else if (whowon == 1) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All the runners have died or given up!");
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

    //method to determine if someone is a runner
    public boolean isRunner(Player player) {
        for (int i = 0 ; i < runners.size() ; i ++) {
            if (runners.get(i).player == player) {
                return true;
            }
        }
        return false;
    }

    public int getRunner(Player player) {
        for (int i = 0 ; i < runners.size() ; i ++) {
            if (runners.get(i).player == player) {
                return i;
            }
        }
        return 0;
    }

    public int getRunnerWithName(String player) {
        for (int i = 0 ; i < runners.size() ; i ++) {
            if (runners.get(i).player.getDisplayName().equals(player)) {
                return i;
            }
        }
        return 0;
    }

    public int runnerLives(Player player) {
        for (int i = 0 ; i < runners.size() ; i ++) {
            if (runners.get(i).player == player) {
                return runners.get(i).lives;
            }
        }
        return 0;
    }


    //method to stop the timer
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID3);
    }

    //create dummy
    public void dummy(Player player) {




        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(),EntityType.ARMOR_STAND);
        disconnected_players.add(armorStand);
        armorStand.setArms(true);
        armorStand.setHelmet(getHead(player));
        armorStand.setVisible(true);
        armorStand.setCustomName(player.getDisplayName());

            if (player.getInventory().getBoots() == null) {
                armorStand.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            } else {
                armorStand.setBoots(new ItemStack(player.getInventory().getBoots()));
            }

            if (player.getInventory().getLeggings() == null) {
                armorStand.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            } else {
                armorStand.setLeggings(new ItemStack(player.getInventory().getLeggings()));
            }

            if (player.getInventory().getChestplate() == null) {
                armorStand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            } else {
                armorStand.setChestplate(new ItemStack(player.getInventory().getChestplate()));
            }


        armorStand.setCustomNameVisible(true);
        armorStand.setHealth(50);



    }



    public void removedummy (Player player) {

        for (int i = 0 ; i < disconnected_players.size() ; i++) {
            if (disconnected_players.get(i).getCustomName().equals(player.getDisplayName())) {
                disconnected_players.get(i).remove();
                disconnected_players.remove(i);
                i = disconnected_players.size();
            }
        }


    }



    //method that gets random number between one and an array length
    public int generaterandom() {

        int max = players.size() - 1;

        int min = 0;


        int range = (max - min) + 1;
        return (int)((Math.random() * range) + min);

    }

    private static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getDisplayName());
        meta.setDisplayName(player.getDisplayName());
        item.setItemMeta(meta);
        return (item);
    }




















}
