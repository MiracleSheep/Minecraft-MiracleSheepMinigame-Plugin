/**
 * Description: This is a class for game commands. It listens for the user to enter a command and calls code based on the command
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//Name of the package this script is in
package com.MiracleSheep.MinigamePlugin.Commands;

//importing libraries and other packages
import com.MiracleSheep.MinigamePlugin.Games.*;
import com.MiracleSheep.MinigamePlugin.Inventory.MainMenu;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//this is the class that contains the command of the plugin
public class PluginCommands implements CommandExecutor {

    //creating an instsance of the main plugin class
    private final MinigamePlugin main;



    //Passing the commands to the main instance
    public PluginCommands(MinigamePlugin main) {
        this.main = main;
    }


    //This searches for the event when a player enters a command and calls the code within
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        GameManager manager = new GameManager(main);
        BlockHunt blockhunt  = new BlockHunt(main);
        ManHunt manhunt = new ManHunt(main);
        ManSwap manswap = new ManSwap(main);

        //Checking if the sender of the command is not a player and returning if true
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players And CommandBlocks can use that command");
            return true;
        }

        //Checking if the commmand sent is equal to the string and calling if it is
        if (cmd.getName().equalsIgnoreCase("minigame")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            //opening the menu when the player enters this command
            MainMenu gui = new MainMenu(main);
            player.openInventory(gui.getInventory());

        }


        //This is a join command
        if (cmd.getName().equalsIgnoreCase("join")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            if (player == manager.getStartPlayer()) {
                player.sendMessage(ChatColor.GREEN + "You are the host of this game! There is no need to join it.");
            } else {


            if (manager.getGame() != 0) {



            if (!manager.players.contains(player)) {


                if (manager.getGame() != 0 && manager.getGameState() == GameState.WAITING) {
                    player.sendMessage(ChatColor.GREEN + "Joining you to the game...");
                    manager.addPlayer(player);
                } else if ( manager.getGameState() != GameState.WAITING) {
                    player.sendMessage(ChatColor.RED + "The game is currently in progress and cannot be joined!");
                } else if (manager.getGame() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no game currently active.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a game!");
            }
            } else {
                player.sendMessage(ChatColor.RED + "There is no currently on going game!");
            }
            }



        }

        //This is a join command
        if (cmd.getName().equalsIgnoreCase("quit")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            if (manager.players.contains(player)) {

                if (manager.getGame() != 0) {
                    player.sendMessage(ChatColor.GREEN + "Leaving " + manager.getName() + "...");
                    if (manager.getGame() == 1) {
                        blockhunt.playerQuit(player);
                    } else if (manager.getGame() == 2) {
                        manhunt.playerQuit(player);
                    } else if (manager.getGame() == 3) {
                        manswap.playerQuit(player);
                    }
                } else if (manager.getGame() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no game currently active.");
                }

            } else {
                player.sendMessage(ChatColor.RED + "You are not in a game! There is nothing to quit!");
            }


        }


        //This is a start command to start the minigame
        if (cmd.getName().equalsIgnoreCase("start")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            if (manager.getGame() != 0) {


                if (player == manager.getStartPlayer()) {

                    if (manager.players.size() >= 2) {



                    if (manager.players.contains(player)) {

                        if (manager.getGame() != 0 && manager.getGameState() == GameState.WAITING) {
                            player.sendMessage(ChatColor.GREEN + "Starting " + manager.getName() + "...");
                            if (manager.getGame() == 1) {
                                blockhunt.setState(GameState.STARTING);
                            } else if (manager.getGame() == 2) {
                                manhunt.setState(GameState.STARTING);
                            } else if (manager.getGame() == 3) {
                                manswap.setState(GameState.STARTING);
                            }

                        } else if (manager.getGameState() != GameState.WAITING) {
                            player.sendMessage(ChatColor.RED + "The current game has already been started!");
                        } else {
                            player.sendMessage(ChatColor.RED + "There is no game currently active.");
                        }

                    } else {
                        player.sendMessage(ChatColor.RED + "You are not in a game! There is nothing to quit!");
                    }

                    } else {
                        player.sendMessage(ChatColor.RED + "There are not enough players to start the game!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not own this game! You are not allowed to start it!");
                }

                } else {
                    player.sendMessage(ChatColor.RED + "There is nothing to start!");
                }


        }


        //This is a cancel command
        if (cmd.getName().equalsIgnoreCase("cancel")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            if (manager.getGame() != 0) {



            if (manager.players.contains(player)) {

                if (player == manager.getStartPlayer()) {



                if (manager.getGame() != 0) {
                    player.sendMessage(ChatColor.GREEN + "Cancelling " + manager.getName() + "...");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + manager.getStartPlayer().getDisplayName() + " has canceled " + manager.getName() + "!");
                    if (manager.getGame() == 1) {
                        blockhunt.setState(GameState.INACTIVE);
                    } else if (manager.getGame() == 2) {
                        manhunt.setState(GameState.INACTIVE);
                    } else if (manager.getGame() == 3) {
                        manswap.setState(GameState.INACTIVE);
                    }
                } else if (manager.getGame() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no game currently active.");
                }
                } else {
                    player.sendMessage(ChatColor.RED + "You are not the host of the game! you cannot cancel it.");
                }

            } else {
                player.sendMessage(ChatColor.RED + "You are not in a game! There is nothing to quit!");
            }
            } else {
                player.sendMessage(ChatColor.RED + "There is no game currently running!");
            }


        }

        //This is a command that lets people change their role to different games
        if (cmd.getName().equalsIgnoreCase("hunter")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            if (manhunt.getGame() == 2) {

                if (manhunt.players.contains(player)) {


                } else {
                    player.sendMessage(ChatColor.RED + "You are not in a game!");
                }

            } else {
                player.sendMessage(ChatColor.RED + "There is no manhunt game being played!");
            }




        }








        //Returning from the function if none of the commands matched
        return true;
    }





}
