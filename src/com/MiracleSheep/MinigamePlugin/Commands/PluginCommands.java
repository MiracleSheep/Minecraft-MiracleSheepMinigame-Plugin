/**
 * Description: This is a class for game commands. It listens for the user to enter a command and calls code based on the command
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//Name of the package this script is in
package com.MiracleSheep.MinigamePlugin.Commands;

//importing libraries and other packages
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//this is the class that contains the command of the plugin
public class PluginCommands implements CommandExecutor {

    //creating an intsance of the main plugin class
    private final MinigamePlugin main;

    //Passing the commands to the main instance
    public PluginCommands(MinigamePlugin main) {
        this.main = main;
    }


    //This searches for the event when a player enters a command and calls the code within
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Checking if the sender of the command is not a player and returning if true
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players And CommandBlocks can use that command");
            return true;
        }

        //Checking if the commmand sent is equal to the string and calling if it is
        if (cmd.getName().equalsIgnoreCase("test")) {

            //Getting the sender of the command
            Player player = (Player) sender;

            //Example code
            player.sendMessage(ChatColor.GREEN + "The test has been answered");

        }

        //Returning from the function if none of the commands matched
        return true;
    }





}
