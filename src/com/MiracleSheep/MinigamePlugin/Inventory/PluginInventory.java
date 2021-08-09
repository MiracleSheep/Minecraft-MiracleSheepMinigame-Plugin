/**
 * Description: This is a class responsible for creating and showing all of the custom inventories in the game. This class is optional
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//Name of the package
package com.MiracleSheep.MinigamePlugin.Inventory;

//Libraries and Packages
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;


//This is the class for the inventory (optional). There can be one or multiple classes for each inventory
public class PluginInventory implements InventoryHolder {

    //Creating an inventory object
    public Inventory inv;
    //Creating an instance of the main class
    private final MinigamePlugin main;
    int inventorysize = 18;


    //This is the constructor
    public PluginInventory(MinigamePlugin main) {

        //initializing the inventory
        inv = Bukkit.createInventory(this,inventorysize,"Plugin Inventory");//max size 54
        this.main = main;
        init(this.main);

    }

    //initialization function for the inventory
    private void init(MinigamePlugin main) {

        //Creating an item called item
        ItemStack item;

        //Fills the inventory with stained glass called none
        for (int i = 0; i < inventorysize ; i++) {
            item = createItem("None",Material.WHITE_STAINED_GLASS_PANE, Collections.singletonList("Nothing"));
            inv.setItem(i,item);
        }


    }


    //This method creates an item with the parameters name, material, and lore
    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }

    //This method is used to open the inventory
    @Override
    public Inventory getInventory() {
        return inv;
    }
}


