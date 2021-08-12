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
public class MainMenu implements InventoryHolder {

    //Creating an inventory object
    public Inventory inv;
    //Creating an instance of the main class
    private final MinigamePlugin main;
    int inventorysize = 45;


    //This is the constructor
    public MainMenu(MinigamePlugin main) {

        //initializing the inventory
        inv = Bukkit.createInventory(this,inventorysize,"Game Menu");//max size 54
        this.main = main;
        init(this.main);

    }


    //initialization function for the inventory
    private void init(MinigamePlugin main) {

        //Creating an item for the White stained glass panes
        ItemStack none = createItem("None",Material.WHITE_STAINED_GLASS_PANE, Collections.singletonList("Please select an option"));;
        //This is an item for yellow stained glass rimming the outsite
        ItemStack rim = createItem("None",Material.YELLOW_STAINED_GLASS_PANE, Collections.singletonList("Please select an option"));;
        //This is an item for the block hunt icon
        ItemStack blockHunt = createItem("Block Hunt",Material.DIAMOND_BLOCK, Collections.singletonList("Select this to play Block Hunt!"));;
        //This is an item for the manhunt icon
        ItemStack manHunt = createItem("Manhunt",Material.COMPASS, Collections.singletonList("Select this to play Manhunt!"));;
        //This is an item for the ManSwap minigame
        ItemStack manSwap = createItem("Man Swap",Material.LAVA_BUCKET, Collections.singletonList("Select this to play Man Swap!"));
        //This is an the closing icon
        ItemStack close = createItem("Close menu",Material.BARRIER, Collections.singletonList("Select this to close the menu."));

        //Creating inventory rows
        int rowNum = inventorysize/9;


        //Fills the inventory
        //This loop iterates through the rows of the chest
        for (int f = 0; f < rowNum; f++) {
            //This loop iterates through every spot in the row
            for (int i = 0; i < 9 ; i++) {

                //Setting every slot to none first before checking if a rim or selecting object needs tro be placed
                inv.setItem(i + (f * 9), none);

                //Checking if the row is the last or beginning row to line it iwth the rim object
                if (f == 0) {
                    inv.setItem(i + (f * 9), rim);
                } else if (f == rowNum - 1) {
                    inv.setItem(i + (f * 9), rim);
                }

                //Checking if the spot selected is the last or starting slot in the row and adding a rim object if true
                if (i == 0 || i == 8) {
                    inv.setItem(i + (f * 9), rim);
                }

                //Placing the selecting objects
                if (f == (((rowNum + 1) / 2) - 1)) {
                    if (i == 3) {
                        inv.setItem(i + (f * 9), blockHunt);
                    } else if (i == 4) {
                        inv.setItem(i + (f * 9), manHunt);
                    } else if (i == 5) {
                        inv.setItem(i + (f * 9), manSwap);
                    }

                }

            }
            }

            inv.setItem(inventorysize - 1, close);
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


