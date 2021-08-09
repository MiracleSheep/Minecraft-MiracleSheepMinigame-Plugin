/**
 * Description: This is the main class of the plugin. It is responsible for calling all of the other classes on enable and disable of the plugin.
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//The package that this script is in
package com.MiracleSheep.MinigamePlugin;

//importing from other packages and libraries
import com.MiracleSheep.MinigamePlugin.Commands.PluginCommands;
import com.MiracleSheep.MinigamePlugin.Events.PluginEvents;
import com.MiracleSheep.MinigamePlugin.Inventory.PluginInventory;
import com.MiracleSheep.MinigamePlugin.Items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

//Main plugin class
public class MinigamePlugin extends JavaPlugin {


    //creating an object that creates items. This is optional
    public ItemManager i = new ItemManager(this);
    //This is a optional object in case the plugin requires an inventory
    public static PluginInventory inventory;

    //code that gets run when the plugin is enabled
    @Override
    public void onEnable() {

        //This creates a config file (optionnal)
        saveDefaultConfig();

        //This checks the config as an example to see if the plugin is enabled
        if (getConfig().getBoolean("Enabled") == true) {
            //This calls the command class and enables the commands for the plugin
            PluginCommands command = new PluginCommands(this);
            //This calles the itemanager class and enables items for the plugin
            ItemManager i = new ItemManager(this);
            ItemManager.init();
            //This registers events for the plugin by called the event class
            getServer().getPluginManager().registerEvents(new PluginEvents(this, inventory), this);
            //Use this line as a template for when adding a new command. It adds the command to the plugin Simply copy paste the line and replace test with what the user needs to type in
            getCommand("test").setExecutor(command);
            //Sending a message to show the plugin is enabled
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PluginTemplate] plugin is enabled.");

        }
    }

    //This function is called when the plugin is disabled (usually when the server turns off, but plugins can be disabled while in-game)
    @Override
    public void onDisable() {

    }

}


