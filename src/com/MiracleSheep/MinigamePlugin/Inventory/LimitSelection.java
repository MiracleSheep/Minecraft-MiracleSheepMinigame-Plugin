/**
 * Description: This is a class responsible for creating and showing all of the custom inventories in the game. This class is optional
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */

package com.MiracleSheep.MinigamePlugin.Inventory;

import com.MiracleSheep.MinigamePlugin.Games.ManHunt;
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LimitSelection implements InventoryHolder {

    //Creating an inventory object
    public Inventory inv;
    //Creating an instance of the main class
    private final MinigamePlugin main;
    int inventorysize = 45;

    //This is the constructor
    public LimitSelection(MinigamePlugin main) {

        //initializing the inventory
        inv = Bukkit.createInventory(this, inventorysize, "Game limit?");//max size 54
        this.main = main;
        init(this.main);

    }

    //initialization function for the inventory
    private void init(MinigamePlugin main) {

        //Creating an item for the White stained glass panes
        ItemStack none = createItem("None", Material.WHITE_STAINED_GLASS_PANE, Collections.singletonList("Please select an option"));
        ;
        //This is an item for yellow stained glass rimming the outsite
        ItemStack rim = createItem("None", Material.YELLOW_STAINED_GLASS_PANE, Collections.singletonList("Please select an option"));

        //This is an the confirming icon
        ItemStack cancel = createItem("Cancel Selection", Material.BARRIER, Collections.singletonList("Select this to cancel the limit selection."));

        //This item sets the number of lives to 1
        ItemStack border = createItem("Border", Material.IRON_BARS, Collections.singletonList("Make a world border that includes the end stronghold."));

        //This item sets the number of lives to 2
        ItemStack teleport = createItem("Teleport", Material.ENDER_PEARL, Collections.singletonList("Teleports hunters closer if they are too far away upon clicking the compass."));

        //This item sets the number of lives to 3
        ItemStack freedom = createItem("Freedom", Material.ELYTRA, Collections.singletonList("No limits, runners can run as far as they want."));



        //Creating inventory rows
        int rowNum = inventorysize / 9;

        //making a variable that gets the number of players in the game
        ManHunt manhunt = new ManHunt(main);
        //checking what players are available
        ArrayList<Player> available_player_list = new ArrayList<Player>();

        //for loop that will put all the available players in the list
        for (int g = 0; g < manhunt.players.size(); g++) {
            if (manhunt.hunters.contains(manhunt.players.get(g))) {

            } else {
                available_player_list.add(manhunt.players.get(g));
            }
        }
        int playernum = 0;


        //Fills the inventory
        //This loop iterates through the rows of the chest
        for (int f = 0 ; f < rowNum; f++) {
            //This loop iterates through every spot in the row
            for (int i = 0; i < 9; i++) {

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






            }
        }

        //placing the lives
        inv.setItem(3 + (9*2), border);
        inv.setItem(4 + (9*2), teleport);
        inv.setItem(5 + (9*2), freedom);

        inv.setItem(inventorysize - 1, cancel);
    }



    //This method creates an item with the parameters name, material, and lore
    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
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




