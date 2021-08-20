/**
 * Description: This is a class responsible for creating any extra items that the plugin may have. This class is optional.
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//Name of the package
package com.MiracleSheep.MinigamePlugin.Items;


//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.Games.ManHunt;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

//This is the class that create items for the game that can be crafted or summoned with commands (optionnal)
public class ItemManager {

    //creating an item called testitem
    public static ItemStack tracker;
    //creating an instance of the main class
    public static MinigamePlugin main;

    //This passes information to the main class
    public ItemManager (MinigamePlugin main) {
        this.main = main;

    }




//    //this function initializes all the items needed by calling their create functions
//    public static void init() {
//        createTracker();
//    }



    //This method is responsable for creating a single item it creates a nether star in this example
    public static void createTracker() {
        ManHunt manhunt = new ManHunt(main);
        //creating the item and what it appears as and amount
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        //modifying the meta of the item
        CompassMeta meta = (CompassMeta) item.getItemMeta();
        //modifying lore
        List<String> lore = new ArrayList<>();
        lore.add("§3Right click this to point to the current coordinates of the player");
        meta.setLore(lore);
        //adding enchantments
        meta.addEnchant(Enchantment.LUCK, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        Location l = manhunt.runner.getLocation();
        l.setY(0);
        l.getBlock().setType(Material.LODESTONE);
        meta.setCustomModelData(123);
        meta.setDisplayName("Tracker");
        meta.setLodestone(l);
        meta.setLodestoneTracked(true);
        item.setItemMeta(meta);
        //setting the class variable testitem to the item in this function
        tracker = item;

    }




}
