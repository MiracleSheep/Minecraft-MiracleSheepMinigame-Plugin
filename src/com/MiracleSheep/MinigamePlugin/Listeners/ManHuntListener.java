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



                if (player != manhunt.runners) {




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

            if (manhunt.runners.contains(player)) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The runner " + player.getDisplayName() + "" + ChatColor.GOLD + " has been killed!");
                manhunt.playerElim(player);

            } else {
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

                if (player != manhunt.runners) {
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

                if (player != manhunt.runners && manhunt.started == false) {
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

                if (player != manhunt.runners) {
                    if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && e.getCurrentItem().getItemMeta().hasCustomModelData() && e.getCurrentItem().getType() == i.tracker.getType()) {
                        e.setCancelled(true);
                    }
                }

            }
        }

        //This checks if the click was in an inventory
        if (e.getClickedInventory() == null) { return; }

        //Checking if clicked in the selection screen
        if (e.getClickedInventory().getHolder() instanceof HunterSelection) {
            e.setCancelled(true);

            if (manhunt.getGame() != 0) {
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "There is currently an ongoing game.");
                player.sendMessage(ChatColor.GREEN + "Please wait for it to finish before starting a new one.");

            } else {


                //Checking what the player clicked on
                if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.RED + "This is not an option");
                    player.closeInventory();
                }  else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.GREEN + "Cancelled hunter selection...");
                    player.closeInventory();
                    manhunt.runners.clear();
                    manhunt.hunters.clear();
                }  else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.GREEN + "Confirmed hunter selection!");
                    manhunt.setState(GameState.STARTING);
                    player.closeInventory();


                for (int j = 0 ; j < Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]).length; j++) {
                    if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        manhunt.hunters.add(player);
                        player.closeInventory();
                        HunterSelection gui = new HunterSelection(main);
                        player.openInventory(gui.getInventory());
                    }
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

                if (player != manhunt.runners) {

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

