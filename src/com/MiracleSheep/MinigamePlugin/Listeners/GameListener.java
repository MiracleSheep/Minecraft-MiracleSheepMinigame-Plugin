/**
 * Description: This class is responsable for listening to events that start the game
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */



//Name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.Games.*;
import com.MiracleSheep.MinigamePlugin.Inventory.*;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.ObjectTypes.ManHuntPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

//this is the class that listens for events
public class GameListener implements Listener {

    //getting an instance of the main class
    public MinigamePlugin main;
    //getting an instance of the main inventory class
    public static MainMenu inventory;

    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public GameListener(MinigamePlugin main, MainMenu m) {
        this.main = main;
        this.inventory = m;

    }

    //This is the event that detects when somebody disconnects
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        BlockHunt blockhunt  = new BlockHunt(main);
        ManHunt manhunt = new ManHunt(main);
        DeathSwap manswap = new DeathSwap(main);
        GameManager manager = new GameManager(main);


        //getting the player who disconnected
        Player player = (Player) e.getPlayer();

        //checking if the player is in the lobby
        if (manager.players.contains(player)) {
            if (manager.getGame() == 1) {
                blockhunt.playerDisc(player);
            } else if (manager.getGame() == 2) {
                manhunt.playerDisc(player);
                if (manhunt.isRunner(player)) {
                    manhunt.runners.get(manhunt.getRunner(player)).disconnect_location = player.getLocation();
                    manhunt.dummy(player);
                }

            } else if (manager.getGame() == 3) {
                manswap.playerDisc(player);
            }
        }

    }

    @EventHandler
    public void onPlayerRejoin(PlayerJoinEvent e) {

        BlockHunt blockhunt = new BlockHunt(main);
        DeathSwap deathswap = new DeathSwap(main);
        ManHunt manhunt = new ManHunt(main);
        GameManager manager = new GameManager(main);

        //getting the player who disconnected
        Player player = e.getPlayer();

        //checking if the player is in the lobby
            if (manager.getGame() == 2) {


                for (int i = 0 ; i < manhunt.runners.size() ; i ++ ) {
                    if (manhunt.runners.get(i).player.getDisplayName().equals(player.getDisplayName())) {
                        manhunt.players.remove(manhunt.runners.get(i).player);
                        manhunt.runners.get(i).player = player;
                        manhunt.players.add(player);
                        if (manhunt.runnerKeep == false && manhunt.runners.get(i).lost_a_life == true) {
                            player.getInventory().clear();
                        }
                        //manhunt.runners.get(i).disconnect_location.setY(player.getWorld().getHighestBlockAt(manhunt.runners.get(i).disconnect_location.getBlockX(),manhunt.runners.get(i).disconnect_location.getBlockZ()).getY() + 1);
                        player.teleport(manhunt.runners.get(i).disconnect_location);
                        manhunt.removedummy(player);

                    }
                }

                for (int i = 0 ; i < manhunt.deadfolk.size() ; i ++ ) {
                    if (manhunt.deadfolk.get(i).getDisplayName().equals(player.getDisplayName())) {
                        manhunt.players.remove(manhunt.deadfolk.get(i));
                        manhunt.deadfolk.remove(i);
                        manhunt.deadfolk.add(player);
                        manhunt.players.add(player);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SPECTATOR);


                    }
                }

                for (int i = 0 ; i < manhunt.hunters.size() ; i ++ ) {
                    if (manhunt.hunters.get(i).getDisplayName().equals(player.getDisplayName())) {
                        manhunt.players.remove(manhunt.hunters.get(i));
                        manhunt.hunters.remove(i);
                        manhunt.hunters.add(player);
                        manhunt.players.add(player);

                    }
                }


            } else if (manager.getGame() == 1) {


                for (int i = 0 ; i < blockhunt.deadfolk.size() ; i ++ ) {
                    if (blockhunt.deadfolk.get(i).getDisplayName().equals(player.getDisplayName())) {
                        blockhunt.players.remove(blockhunt.deadfolk.get(i));
                        blockhunt.deadfolk.remove(i);
                        blockhunt.deadfolk.add(player);
                        blockhunt.players.add(player);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SPECTATOR);


                    }
                }







                for (int i = 0 ; i < blockhunt.playerlist.size() ; i ++ ) {
                    if (blockhunt.playerlist.get(i).player.getDisplayName().equals(player.getDisplayName())) {
                        blockhunt.players.remove(blockhunt.playerlist.get(i).player);
                        blockhunt.playerlist.get(i).player = player;
                        blockhunt.players.add(player);
                        player.sendMessage(ChatColor.AQUA + "Welcome back! Your current block is: " + blockhunt.playerlist.get(i).block);
                    }
                }






            } else if (manager.getGame() == 3) {


                for (int i = 0 ; i < deathswap.playerlist.size() ; i ++ ) {
                    if (deathswap.playerlist.get(i).player.getDisplayName().equals(player.getDisplayName())) {
                        deathswap.players.remove(deathswap.playerlist.get(i).player);
                        deathswap.playerlist.get(i).player = player;
                        deathswap.players.add(player);
                    }
                }

                for (int i = 0 ; i < deathswap.deadfolk.size() ; i ++ ) {
                    if (deathswap.deadfolk.get(i).getDisplayName().equals(player.getDisplayName())) {
                        deathswap.players.remove(deathswap.deadfolk.get(i));
                        deathswap.deadfolk.remove(i);
                        deathswap.deadfolk.add(player);
                        deathswap.players.add(player);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SPECTATOR);


                    }
                }


            }


    }

    //on player respawn event
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        ManHunt manhunt = new ManHunt(main);
        BlockHunt blockhunt = new BlockHunt(main);
        DeathSwap deathswap = new DeathSwap(main);


        if (!(e.getPlayer() instanceof Player)) {return;}
        Player player = (Player) e.getPlayer();

        if (blockhunt.getGameState() == GameState.ACTIVE && blockhunt.getGame() == 1) {
            blockhunt.playerlist.forEach((temp) -> {
                if (temp.player == player) {
                    if (blockhunt.keepinventory) {
                        restoreInventory(player);
                    }
                    e.setRespawnLocation(temp.spawnpoint);
                }
            });

        }

        if (deathswap.getGameState() == GameState.ACTIVE && deathswap.getGame() == 3) {
            deathswap.playerlist.forEach((temp) -> {
                if (temp.player == player) {
                    if (deathswap.keepinventory) {
                        restoreInventory(player);
                    }
                    e.setRespawnLocation(temp.spawnpoint);


                }
            });


        }

        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player) && manhunt.hunterKeep == true) {
                    restoreInventory(player);
                } else if (!(manhunt.hunters.contains(player)) && !(manhunt.deadfolk.contains(player)) && manhunt.runnerKeep == true) {
                    restoreInventory(player);
                }
            }
        }

    }


    //on player death event
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        ManHunt manhunt = new ManHunt(main);
        BlockHunt blockhunt = new BlockHunt(main);
        DeathSwap deathswap = new DeathSwap(main);

        //ccheccking if the person who died wwas a player (this is actually uselsss ccode, but I'm leaving it here because I'm lazy.
        if (!(e.getEntity() instanceof Player)) {return;}
        Player player = (Player) e.getEntity();

        //code for blocckhunt deaths
        if (deathswap.getGameState() == GameState.ACTIVE && deathswap.getGame() == 3) {

            deathswap.playerlist.forEach((temp) -> {
                if (temp.player == player) {

                    if (deathswap.keepinventory) {
                        saveInventory(player);
                        e.getDrops().clear();
                    }

                    deathswap.playerElim(temp);
                    deathswap.isWon();
                }
            });





        }

        if (blockhunt.getGameState() == GameState.ACTIVE && blockhunt.getGame() == 1) {

            blockhunt.playerlist.forEach((temp) -> {
                if (temp.player == player) {

                    if (blockhunt.keepinventory) {
                        saveInventory(player);
                        e.getDrops().clear();
                    }

                }
            });

        }

        //code for manhunt deaths...
        if (manhunt.getGameState() == GameState.ACTIVE && manhunt.getGame() == 2) {

            if (manhunt.players.contains(player)) {

                if (manhunt.hunters.contains(player) && manhunt.hunterKeep == true) {
                    saveInventory(player);
                    e.getDrops().clear();
                }

                if (manhunt.isRunner(player)) {
                    if (manhunt.runnerKeep == true) {
                        saveInventory(player);
                        if (manhunt.runnerLives(player) <= 0) {

                        } else {
                            e.getDrops().clear();
                        }
                    }

                    manhunt.playerElim(player);

                }  else if (!(manhunt.deadfolk.contains(player))) {
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


    //This is an example of an event. When a player clicks in an inventory this function will be called. More events can be found on the spigot webpage
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        BlockHunt blockhunt  = new BlockHunt(main);
        ManHunt manhunt = new ManHunt(main);
        DeathSwap manswap = new DeathSwap(main);

        //This checks if the click was in an inventory
        if (e.getClickedInventory() == null) { return; }
        //Checking if clicked in the selection screen
        if (e.getClickedInventory().getHolder() instanceof MainMenu) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

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
            } else if (e.getCurrentItem().getType() == Material.DIAMOND_BLOCK) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Selecting Block Hunt!");
                player.sendMessage(ChatColor.GREEN + "Searching for players...");
                blockhunt.setStartPlayer(player);
                blockhunt.setState(GameState.WAITING);
                //Calling the block hunt class to start the game
                player.closeInventory();

            }  else if (e.getCurrentItem().getType() == Material.COMPASS) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Selecting Manhunt!");
                player.sendMessage(ChatColor.GREEN + "Searching for players...");
                manhunt.setStartPlayer(player);
                manhunt.setState(GameState.WAITING);
                player.closeInventory();
            } else if (e.getCurrentItem().getType() == Material.LAVA_BUCKET) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Selecting Death Swap!");
                player.sendMessage(ChatColor.GREEN + "Searching for players...");
                player.closeInventory();
                manswap.setStartPlayer(player);
                manswap.setState(GameState.WAITING);
            }  else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Closed menu.");
                player.closeInventory();
            }

            }

        }

        if (e.getClickedInventory().getHolder() instanceof HunterSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

                //Checking what the player clicked on
                if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.RED + "This is not an option");
                } else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    //outputting the choice to the player

                    try {
                        manhunt.hunters.add(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + e.getCurrentItem().getItemMeta().getDisplayName() + " has been selected as a Hunter!");
                    } catch (Exception b) {
                        player.sendMessage(ChatColor.GREEN + "There was an error making this player a hunter");
                    }
                        player.closeInventory();
                    HunterSelection gui = new HunterSelection(main);
                    player.openInventory(gui.getInventory());

                }  else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                    ManHunt.hunters.clear();
                    ManHunt.lives = 0;
                    ManHunt.limit = 0;
                    player.closeInventory();
                } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                    //outputting the choice to the player
                    player.sendMessage(ChatColor.GREEN + "Confirmed hunter selection!");
                    player.closeInventory();
                    manhunt.setStartPlayer(player);
                    manhunt.setState(GameState.STARTING);

                }



        }

        if (e.getClickedInventory().getHolder() instanceof ManhuntCompassType) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.COMPASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The compass will track the nearest player to the hunter.");
                manhunt.trackindividuals = false;
                player.closeInventory();
                HunterSelection gui = new HunterSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.MAP) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Hunters will be able to choose the runner they track.");
                manhunt.trackindividuals = true;
                player.closeInventory();
                HunterSelection gui = new HunterSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manhunt.hunters.clear();
                manhunt.lives = 0;
                manhunt.limit = 0;
                player.closeInventory();
            }



        }


        if (e.getClickedInventory().getHolder() instanceof LifeSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                ManHunt.lives = 1;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each runner will have 1 life.");
                player.closeInventory();
                HunterSaveInvSelection gui = new HunterSaveInvSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.YELLOW_WOOL) {
                ManHunt.lives = 2;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each runner will have 2 lives.");
                player.closeInventory();
                HunterSaveInvSelection gui = new HunterSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            }  else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each runner will have 3 lives.");
                player.closeInventory();
                ManHunt.lives = 3;
                HunterSaveInvSelection gui = new HunterSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            }   else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                ManHunt.hunters.clear();
                ManHunt.lives = 0;
                ManHunt.limit = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof LimitSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.IRON_BARS) {

                ManHunt.limit = 1;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There will be a world border that encompasses the stronghold.");
                player.closeInventory();
                LifeSelection gui = new LifeSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {

                ManHunt.limit = 2;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Hunters will be teleported closer to Runners if they are too far upon right clicking their compass");
                player.closeInventory();
                LifeSelection gui = new LifeSelection(main);

                player.openInventory(gui.getInventory());
            }  else if (e.getCurrentItem().getType() == Material.ELYTRA) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There will be no limits. Runners may go as far as they please.");
                player.closeInventory();
                ManHunt.limit = 0;
                LifeSelection gui = new LifeSelection(main);
                player.openInventory(gui.getInventory());
            }   else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                ManHunt.hunters.clear();
                ManHunt.lives = 0;
                ManHunt.limit = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof RunnerSaveInvSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Runners will lose their inventory on death.");
                manhunt.runnerKeep = false;
                player.closeInventory();
                ManhuntCompassType gui = new ManhuntCompassType(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Runners will keep their inventory on death.");
                manhunt.runnerKeep = true;
                player.closeInventory();
                ManhuntCompassType gui = new ManhuntCompassType(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manhunt.hunters.clear();
                manhunt.lives = 0;
                manhunt.limit = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof HunterSaveInvSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Hunters will lose their inventory on death.");
                manhunt.hunterKeep = false;
                player.closeInventory();
                RunnerSaveInvSelection gui = new RunnerSaveInvSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Hunters will keep their inventory on death.");
                manhunt.hunterKeep = true;
                player.closeInventory();
                RunnerSaveInvSelection gui = new RunnerSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manhunt.hunters.clear();
                manhunt.lives = 0;
                manhunt.limit = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof BlockHuntDifficultySelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There will be 20 blocks between each difficulty change.");
                blockhunt.difficulty = 20;
                player.closeInventory();
                BlockHuntLifeSelection gui = new BlockHuntLifeSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.YELLOW_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There will be 15 blocks between each difficulty change.");
                blockhunt.difficulty = 15;
                player.closeInventory();
                BlockHuntLifeSelection gui = new BlockHuntLifeSelection(main);
                player.openInventory(gui.getInventory());
            }else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: There will be 10 blocks between each difficulty change.");
                blockhunt.difficulty = 10;
                player.closeInventory();
                BlockHuntLifeSelection gui = new BlockHuntLifeSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                blockhunt.difficulty = 0;
                player.closeInventory();
            }



        }


        if (e.getClickedInventory().getHolder() instanceof BlockHuntLifeSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have one life.");
                blockhunt.lives = 1;
                player.closeInventory();
                BlockHuntSaveInvSelection gui = new BlockHuntSaveInvSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.YELLOW_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have two lives.");
                blockhunt.lives = 2;
                player.closeInventory();
                BlockHuntSaveInvSelection gui = new BlockHuntSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            }else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have three lives.");
                blockhunt.lives = 3;
                player.closeInventory();
                BlockHuntSaveInvSelection gui = new BlockHuntSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                blockhunt.difficulty = 0;
                blockhunt.lives = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof BlockHuntSaveInvSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will not keep their inventory on death.");
                blockhunt.keepinventory = false;
                player.closeInventory();
                BlockHuntTeleportSelection gui = new BlockHuntTeleportSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will have their inventories kept upon death.");
                blockhunt.keepinventory = true;
                player.closeInventory();
                BlockHuntTeleportSelection gui = new BlockHuntTeleportSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                blockhunt.difficulty = 0;
                blockhunt.lives = 0;
                blockhunt.keepinventory = false;
                player.closeInventory();
            }



        }


        if (e.getClickedInventory().getHolder() instanceof BlockHuntTeleportSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will not be distributed when the game starts.");
                blockhunt.teleport = false;
                player.closeInventory();
                BlockHuntTimerSelection gui = new BlockHuntTimerSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will be distributed when the game starts.");
                blockhunt.teleport = true;
                player.closeInventory();
                BlockHuntTimerSelection gui = new BlockHuntTimerSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                blockhunt.difficulty = 0;
                blockhunt.lives = 0;
                blockhunt.keepinventory = false;
                blockhunt.teleport = false;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof BlockHuntTimerSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.DAYLIGHT_DETECTOR) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The time given to find the blocks will not change.");

                blockhunt.speed = false;
                player.closeInventory();
                BlockHuntSameBlocks gui = new BlockHuntSameBlocks(main);
                player.openInventory(gui.getInventory());
            }  else if (e.getCurrentItem().getType() == Material.CLOCK) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: The time given to find the blocks will slowly decrease.");
                blockhunt.speed = true;
                player.closeInventory();
                BlockHuntSameBlocks gui = new BlockHuntSameBlocks(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                blockhunt.difficulty = 0;
                blockhunt.lives = 0;
                blockhunt.keepinventory = false;
                blockhunt.teleport = false;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof DeathSwapLifeSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have one life.");
                manswap.lives = 1;
                player.closeInventory();
                DeathSwapSaveInvSelection gui = new DeathSwapSaveInvSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.YELLOW_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have two lives.");
                manswap.lives = 2;
                player.closeInventory();
                DeathSwapSaveInvSelection gui = new DeathSwapSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            }else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Each player will have three lives.");
                manswap.lives = 3;
                player.closeInventory();
                DeathSwapSaveInvSelection gui = new DeathSwapSaveInvSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manswap.lives = 0;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof DeathSwapSaveInvSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will not keep their inventory on death.");
                manswap.keepinventory = false;
                player.closeInventory();
                DeathSwapTeleportSelection gui = new DeathSwapTeleportSelection(main);
                player.openInventory(gui.getInventory());

            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will have their inventories kept upon death.");
                manswap.keepinventory = true;
                player.closeInventory();
                DeathSwapTeleportSelection gui = new DeathSwapTeleportSelection(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manswap.lives = 0;
                manswap.keepinventory = false;
                manswap.dimensions = true;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof DeathSwapTeleportSelection) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will not be distributed when the game starts.");
                manswap.teleport = false;
                player.closeInventory();
                DeathSwapBanDimensions gui = new DeathSwapBanDimensions(main);
                player.openInventory(gui.getInventory());
            }  else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will be distributed when the game starts.");
                manswap.teleport = true;
                player.closeInventory();
                DeathSwapBanDimensions gui = new DeathSwapBanDimensions(main);
                player.openInventory(gui.getInventory());
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");
                manswap.lives = 0;
                manswap.keepinventory = false;
                manswap.teleport = false;
                manswap.dimensions = true;
                player.closeInventory();
            }



        }



        if (e.getClickedInventory().getHolder() instanceof BlockHuntSameBlocks) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.FIREWORK_ROCKET) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All players will be given different blocks to find.");
                blockhunt.sameblock = false;
                player.closeInventory();
                blockhunt.setState(GameState.STARTING);
            }  else if (e.getCurrentItem().getType() == Material.FIREWORK_STAR) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: All players will be given the same blocks to find.");
                blockhunt.sameblock = true;
                player.closeInventory();
                blockhunt.setState(GameState.STARTING);
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");

                blockhunt.lives = 0;
                blockhunt.keepinventory = false;
                blockhunt.teleport = false;
                blockhunt.speed = false;
                blockhunt.sameblock = false;
                player.closeInventory();
            }



        }

        if (e.getClickedInventory().getHolder() instanceof DeathSwapBanDimensions) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            //Checking what the player clicked on
            if (e.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.RED + "This is not an option");
            } else if (e.getCurrentItem().getType() == Material.BEDROCK) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will not be allowed access to the Dimensions.");
                manswap.dimensions = false;
                player.closeInventory();
                manswap.setState(GameState.STARTING);
            }  else if (e.getCurrentItem().getType() == Material.NETHERRACK) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Players will be allowed to enter the Dimensions.");
                manswap.dimensions = true;
                player.closeInventory();
                manswap.setState(GameState.STARTING);
            } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                //outputting the choice to the player
                player.sendMessage(ChatColor.GREEN + "Canceled option selection!");

                manswap.lives = 0;
                manswap.keepinventory = false;
                manswap.teleport = false;
                manswap.dimensions = true;
                player.closeInventory();
            }



        }



    }

    //This event detects when the player enters a new dimension
    @EventHandler
    public void onDimensionChange(PortalCreateEvent e) {
        //instance of deathswap to refer to it's methods
        DeathSwap manswap = new DeathSwap(main);


        if (manswap.dimensions == false) {
            e.setCancelled(true);
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

}






