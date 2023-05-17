/**
 * Description: this class is responsable for listening to events from the Man Hunt minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//This is the name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//These are the required librairies and packages
import com.MiracleSheep.MinigamePlugin.Games.BlockHunt;
import com.MiracleSheep.MinigamePlugin.Games.DeathSwap;
import com.MiracleSheep.MinigamePlugin.Games.GameState;
import com.MiracleSheep.MinigamePlugin.Games.ManHunt;
import com.MiracleSheep.MinigamePlugin.Inventory.HunterSelection;
import com.MiracleSheep.MinigamePlugin.Inventory.MainMenu;
import com.MiracleSheep.MinigamePlugin.Items.ItemManager;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.ObjectTypes.ManHuntPlayer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

//this is the class that listens for hunt events
public class ManHuntListener implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;

    public ItemManager i = new ItemManager(main);

    public static Map<String,Long> cooldowns;

    public static int cooldowntime = 500;


    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public ManHuntListener(MinigamePlugin main) {
        this.main = main;
    }

    //This event detects when players move
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        ManHunt manhunt = new ManHunt(main);

        //getting the player who moved
        Player player = (Player) e.getPlayer();

        if (manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.getGameState() == GameState.ACTIVE) {



                if (manhunt.hunters.contains(player)) {

                    if (manhunt.started == false) {

                        e.setCancelled(true);

                    }

                    if (player.getInventory().contains(i.tracker.getType()) || player.getInventory().getItemInOffHand().getType() == i.tracker.getType()) {
                        boolean is_in_another_dimension = true;
                        for (int i = 0; i < manhunt.runners.size(); i++) {
                            if (manhunt.runners.get(i).player.getWorld().getEnvironment().equals(player.getWorld().getEnvironment())) {
                                is_in_another_dimension = false;
                            }
                        }
                        if (is_in_another_dimension == true) {

                        } else {
                            setTracker(player);
                        }
                    } else {
                        i.createTracker(player);
                        player.getInventory().addItem(i.tracker);
                    }



                }
                }

            }

        }


    }

    //THis event detects when a player is hit
    @EventHandler
    public void onPlayerHit(EntityDamageEvent e) {
        ManHunt manhunt = new ManHunt(main);

        if (!(e.getEntity() instanceof Player)) {return;}

        if (manhunt.damage == false) {e.setCancelled(true);}

        Player player = (Player) e.getEntity();

        if (manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.getGameState() == GameState.ACTIVE) {

                    if (manhunt.started == false) {

                        e.setCancelled(true);

                    }
                }

            }

        }
    }


    //clock event for compass


    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent e){
        ManHunt manhunt = new ManHunt(main);
        if(e.getEntity() instanceof EnderDragon){
            if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {
            manhunt.onWon(0);
            }


        }
    }






    //on player throw item event
    @EventHandler
    public void onPlayerThrow(PlayerDropItemEvent e){
        ManHunt manhunt = new ManHunt(main);
        Player player = e.getPlayer();

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player)) {
                    if (e.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()){
                    if (e.getItemDrop().getItemStack().getItemMeta().getCustomModelData() == i.tracker.getItemMeta().getCustomModelData()) {
                        e.setCancelled(true);
                    }
                }

                }
            }

        }




    }


    //on player throw transfer inventory event
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        ManHunt manhunt = new ManHunt(main);
        Player player = e.getPlayer();
        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player) && manhunt.started == false) {
                            e.setCancelled(true);
                    }

                }
            }
    }



    @EventHandler
    public void onArmorStandMurder(EntityDamageEvent e) {

        ManHunt manhunt = new ManHunt(main);

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {
                if (e.getEntity().getType() == EntityType.ARMOR_STAND) {

                    ArmorStand armorStand = (ArmorStand) e.getEntity();
                    Player disconnected_player;

                    for (int i = 0; i < manhunt.runners.size(); i++) {


                        if (armorStand.getCustomName().equals(manhunt.runners.get(i).player.getDisplayName())) {
                            e.setCancelled(true);
                            if (manhunt.started == true) {


                                disconnected_player = manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).player;
                                manhunt.playerElim(disconnected_player);
                                if (manhunt.runnerLives(disconnected_player) <= 0) {
                                    for (int j = 0; j <= 35; j++) {
                                        if (disconnected_player.getInventory().getItem(j) == null) {

                                        } else {
                                            Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getItem(j));
                                        }

                                    }

                                    for (int j = 0; j < disconnected_player.getInventory().getArmorContents().length; j++) {
                                        if (disconnected_player.getInventory().getArmorContents()[j] == null) {

                                        } else {
                                            Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getArmorContents()[j]);
                                        }

                                    }

                                    if (disconnected_player.getInventory().getItemInOffHand() == null) {

                                    } else {
                                        Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getItemInOffHand());
                                    }

                                    if (manhunt.runnerKeep == false) {
                                        armorStand.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                                        armorStand.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                                        armorStand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                                    }


                                    manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).player.getInventory().clear();
                                    manhunt.disconnected_players.remove(armorStand);
                                    armorStand.remove();


                                } else {


                                    if (manhunt.runnerKeep == false) {

                                        for (int j = 0; j <= 35; j++) {
                                            if (disconnected_player.getInventory().getItem(j) == null) {

                                            } else {
                                                Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getItem(j));
                                            }

                                        }

                                        for (int j = 0; j < disconnected_player.getInventory().getArmorContents().length; j++) {
                                            if (disconnected_player.getInventory().getArmorContents()[j] == null) {

                                            } else {
                                                Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getArmorContents()[j]);
                                            }

                                        }

                                        if (disconnected_player.getInventory().getItemInOffHand() == null) {

                                        } else {
                                            Bukkit.getWorld("world").dropItem(armorStand.getLocation(), disconnected_player.getInventory().getItemInOffHand());
                                        }

                                        manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).player.getInventory().clear();
                                        manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).lost_a_life = true;

                                    }


                                    if (manhunt.runnerKeep == false) {
                                        armorStand.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                                        armorStand.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                                        armorStand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                                    }


                                    if (manhunt.runners.get(i).player.getBedSpawnLocation() == null) {
                                        armorStand.teleport(manhunt.runners.get(i).player.getWorld().getSpawnLocation());
                                        manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).disconnect_location = armorStand.getLocation();
                                    } else {
                                        armorStand.teleport(manhunt.runners.get(i).player.getBedSpawnLocation());
                                        manhunt.runners.get(manhunt.getRunnerWithName(armorStand.getCustomName())).disconnect_location = armorStand.getLocation();
                                    }

                                }
                            }
                        }
                    }
                }



        }

    }

    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent e) {


        ManHunt manhunt = new ManHunt(main);
        Player player = (Player) e.getPlayer();


        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
                ArmorStand armorStand = (ArmorStand) e.getRightClicked();
                for (int i = 0 ; i < manhunt.players.size() ; i++) {
                    if (armorStand.getCustomName().equals(manhunt.players.get(i).getDisplayName())) {
                        e.setCancelled(true);
                    }
                }
            }


        }

    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {


        ManHunt manhunt = new ManHunt(main);
        Player player = (Player) e.getWhoClicked();

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player)) {
                    if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && e.getCurrentItem().getItemMeta().hasCustomModelData() && e.getCurrentItem().getType() == i.tracker.getType()) {
                        e.setCancelled(true);
                    }
                }

            }
        }

    }



        //on player throw item event
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){
        ManHunt manhunt = new ManHunt(main);
        Player player = e.getPlayer();

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player)) {

                        if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().getType() == i.tracker.getType()) {

                            if (player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) {


                                if (player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == i.tracker.getItemMeta().getCustomModelData()) {
                                    boolean is_in_another_dimension = true;
                                    for (int i = 0; i < manhunt.runners.size(); i++) {
                                        if (manhunt.runners.get(i).player.getWorld().getEnvironment().equals(player.getWorld().getEnvironment())) {
                                            is_in_another_dimension = false;
                                        }
                                    }
                                    if (is_in_another_dimension == true) {
                                        player.sendMessage(ChatColor.RED + "You are not in the same dimension!");
                                    } else {

                                        if (e.getHand() == EquipmentSlot.HAND) {


                                            if (manhunt.trackindividuals) {
                                                changeTarget(player);
                                                setTracker(player);
                                            } else {

                                                setTracker(player);
                                            }



                                            if (manhunt.limit == 2) {

                                                tp_to_runner(player);
                                            }
                                        }














                                    }



                        }
                    }
                    }
                }
            }

        }




    }

    public void saveInventory(Player player) {
        File f = new File(main.getDataFolder().getAbsolutePath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.armor", player.getInventory().getArmorContents());
        c.set("inventory.content", player.getInventory().getContents());
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreInventory(Player player) {
        File f = new File(main.getDataFolder().getAbsolutePath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
        player.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        player.getInventory().setContents(content);
    }

    public void changeTarget(Player player) {
        ManHunt manhunt = new ManHunt(main);
        for (int j = 0; j <= 35 ; j++) {
            if (player.getInventory().getItem(j).getItemMeta().hasCustomModelData() && player.getInventory().getItem(j).getItemMeta().getCustomModelData() == i.tracker.getItemMeta().getCustomModelData()) {
                CompassMeta meta = (CompassMeta) player.getInventory().getItem(j).getItemMeta();
                int current_enchant_level = meta.getEnchantLevel(Enchantment.LUCK);
                int array_spot = current_enchant_level - 1;
                int future_enchant_level = current_enchant_level + 1;

                if (array_spot + 1 >= manhunt.runners.size()) {
                    meta.removeEnchant(Enchantment.LUCK);
                    meta.addEnchant(Enchantment.LUCK,1,true);
                } else {
                    meta.removeEnchant(Enchantment.LUCK);
                    meta.addEnchant(Enchantment.LUCK,future_enchant_level,true);
                }
            player.getInventory().getItem(j).setItemMeta(meta);
                player.sendMessage(ChatColor.DARK_PURPLE + "Now tracking " + manhunt.runners.get(meta.getEnchantLevel(Enchantment.LUCK) - 1).player.getDisplayName() + "!");
                j = 36;
            }
        }



    }

    public void tp_to_runner(Player player) {

        ManHunt manhunt = new ManHunt(main);
        Location hunterLocation = player.getLocation();

        for (int j = 0; j <= 35 ; j++) {
            if (player.getInventory().getItem(j).getItemMeta().hasCustomModelData() && player.getInventory().getItem(j).getItemMeta().getCustomModelData() == i.tracker.getItemMeta().getCustomModelData()) {
                CompassMeta meta = (CompassMeta) player.getInventory().getItem(j).getItemMeta();
                Location closestLocation = meta.getLodestone();

                    if (closestLocation.distance(hunterLocation) > main.getConfig().getInt("ManhuntTeleportDistance")) {

                        boolean withindistance = false;
                        double desired_x = hunterLocation.getX();
                        double desired_z = hunterLocation.getZ();
                        while(!withindistance) {
                            if (closestLocation.distance(hunterLocation) >= main.getConfig().getInt("ManhuntTeleportDistance")) {
                                if (desired_x < closestLocation.getX()) {
                                    player.sendMessage(ChatColor.RED + "");
                                    desired_x += 10;
                                } else if (desired_x > closestLocation.getX()) {
                                    desired_x -= 10;
                                }
                                if (desired_z < closestLocation.getZ()) {
                                    desired_z += 10;
                                } else if (desired_z > closestLocation.getZ()) {
                                    desired_z -= 10;
                                }
                                hunterLocation.setX(desired_x);
                                hunterLocation.setZ(desired_z);
                            } else {
                                withindistance = true;
                            }

                        }

                        if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {


                        } else {

                            player.teleport(closestLocation.add(main.getConfig().getInt("ManhuntTeleportDistance"),0,0));
                            Location loc = hunterLocation;
                            player.teleport(loc);
                            loc.setY(player.getWorld().getHighestBlockAt( player.getLocation().getBlockX(), player.getLocation().getBlockZ()).getY() + 1);
                            player.teleport(loc);
                        }
                    }
                }



            }


    }

    public void setTracker(Player player) {
        ManHunt manhunt = new ManHunt(main);
        Location hunterLocation = player.getLocation();
        double closestDist = 0;
        Location closestLocation = manhunt.spawn;

        for (int j = 0; j <= 35 ; j++) {
            if (player.getInventory().getItem(j).getItemMeta().hasCustomModelData() && player.getInventory().getItem(j).getItemMeta().getCustomModelData() == i.tracker.getItemMeta().getCustomModelData()) {
                CompassMeta meta = (CompassMeta) player.getInventory().getItem(j).getItemMeta();
                hunterLocation = player.getLocation();
                closestDist = 0;
                closestLocation = manhunt.spawn.clone();

                if (manhunt.trackindividuals) {

                    int current_enchant_level = meta.getEnchantLevel(Enchantment.LUCK);
                    if (manhunt.runners.get(current_enchant_level - 1).player.isOnline()) {
                        closestLocation = manhunt.runners.get(current_enchant_level - 1).player.getLocation().clone();
                    } else {
                        closestLocation = manhunt.runners.get(current_enchant_level - 1).disconnect_location.clone();
                    }


                } else {




                //this list will contain all locations that are valid
                    ArrayList<Location> valid_locations = new ArrayList<Location>();

                    for (int n = 0 ; n < manhunt.runners.size() ; n++) {
                        if (manhunt.runners.get(n).player.isOnline()) {
                            valid_locations.add(manhunt.runners.get(n).player.getLocation().clone());
                        } else {
                            valid_locations.add(manhunt.runners.get(n).disconnect_location.clone());
                        }
                    }


                for (int z = 0 ; z < valid_locations.size() ; z++) {

                    if (valid_locations.get(z).getWorld().getEnvironment() == player.getWorld().getEnvironment()) {
                        closestLocation = valid_locations.get(z);
                        closestDist = hunterLocation.distance(closestLocation);
                    }
                }

                for (int i = 0; i < valid_locations.size(); i++)  {

                    if (valid_locations.get(i).getWorld().getEnvironment() == player.getWorld().getEnvironment()) {

                        if (valid_locations.get(i).distance(hunterLocation) < closestDist) {

                            closestDist = valid_locations.get(i).distance(hunterLocation);
                            closestLocation = valid_locations.get(i);

                        }

                    }



                }


                }

                closestLocation.setY(0);
                closestLocation.getBlock().setType(Material.LODESTONE);
                meta.setLodestone(closestLocation);
                meta.setLodestoneTracked(true);
                player.getInventory().getItem(j).setItemMeta(meta);
                j = 36;

            }
        }



    }

}







