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
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

//this is the class that listens for hunt events
public class ManHuntListener implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;

    public ItemManager i = new ItemManager(main);


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

                    if (player.getInventory().contains(i.tracker.getType()) || player.getInventory().getItemInOffHand().getType() == i.tracker.getType()) {

                    } else {
                        i.createTracker(player);
                        player.getInventory().addItem(i.tracker);
                    }

                    if (manhunt.started == false) {

                        e.setCancelled(true);

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
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Ender Dragon has been defeated!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The Runners have won!");
            manhunt.setState(GameState.INACTIVE);

            }


        }
    }

    //on player death event
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        ManHunt manhunt = new ManHunt(main);
        if (!(e.getEntity() instanceof Player)) {return;}
        Player player = (Player) e.getEntity();

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

            if (!(manhunt.hunters.contains(player)) && !(manhunt.deadfolk.contains(player))) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The runner " + player.getDisplayName() + "" + ChatColor.GOLD + " has been killed!");

                ManHuntPlayer runner;
                //getting the runner and finding the nuber of lives they have
                for (int i = 0 ; i < manhunt.runners.size(); i++)  {
                    if (manhunt.runners.get(i).player == player) {
                         manhunt.runners.get(i).lives -= 1;
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: They have " + manhunt.runners.get(i).lives + "" + ChatColor.GOLD + " lives remaining.");

                         if (manhunt.runners.get(i).lives <= 0) {
                             Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + player.getDisplayName() + "has been eliminated from ManHunt!");
                             manhunt.playerElim(player);
                         }

                    }
                }



            } else if (!(manhunt.deadfolk.contains(player))) {
                for (int j = 0; j < player.getInventory().getSize() ; j++) {
                    ItemStack item = player.getInventory().getItem(j);
                    if (item != null) {


                        if (item.getItemMeta().hasCustomModelData()) {
                            if (item.getItemMeta().getCustomModelData() == main.i.tracker.getItemMeta().getCustomModelData()) {
                                e.getDrops().remove(item);
                            }
                        }
                    }
                }
            }
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
                                        i.createTracker(player);
                                        player.getInventory().setItemInMainHand(i.tracker);
                                    }



                        }
                    }
                    }
                }
            }

        }




    }





}

