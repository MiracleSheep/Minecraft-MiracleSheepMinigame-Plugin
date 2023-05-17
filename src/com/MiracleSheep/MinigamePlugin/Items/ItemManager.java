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
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.CompassMeta;

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
    public static void createTracker(Player hunter) {
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
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        //this part of the code checks all the locations of the runners and the hunter requesting, and then gets the closest player
        //setting the initial location to the first player in the runner list
        Location hunterLocation = hunter.getLocation();
        double closestDist = 0;
        Location closestLocation = manhunt.spawn;
        int initial = 0;

        if (manhunt.trackindividuals) {

            int current_enchant_level = meta.getEnchantLevel(Enchantment.LUCK);
            if (manhunt.runners.get(current_enchant_level - 1).player.isOnline()) {
                closestLocation = manhunt.runners.get(current_enchant_level - 1).player.getLocation();
            } else {
                closestLocation = manhunt.runners.get(current_enchant_level - 1).disconnect_location;
            }


        } else {




            //this list will contain all locations that are valid
            ArrayList<Location> valid_locations = new ArrayList<Location>();

            for (int n = 0 ; n < manhunt.runners.size() ; n++) {
                if (manhunt.runners.get(n).player.isOnline()) {
                    valid_locations.add(manhunt.runners.get(n).player.getLocation());
                } else {
                    valid_locations.add(manhunt.runners.get(n).disconnect_location);
                }
            }


            for (int z = 0 ; z < valid_locations.size() ; z++) {

                if (valid_locations.get(z).getWorld().getEnvironment() == hunter.getWorld().getEnvironment()) {
                    closestLocation = valid_locations.get(z);
                    closestDist = hunterLocation.distance(closestLocation);
                }
            }

            for (int i = 0; i < valid_locations.size(); i++)  {

                if (valid_locations.get(i).getWorld().getEnvironment() == hunter.getWorld().getEnvironment()) {

                    if (valid_locations.get(i).distance(hunterLocation) < closestDist) {

                        closestDist = valid_locations.get(i).distance(hunterLocation);
                        closestLocation = valid_locations.get(i);

                    }

                }



            }


        }

        closestLocation.setY(0);
        closestLocation.getBlock().setType(Material.LODESTONE);
        meta.setCustomModelData(123);
        meta.setDisplayName("Tracker");
        meta.setLodestone(closestLocation);
        meta.setLodestoneTracked(true);
        item.setItemMeta(meta);
        //setting the class variable testitem to the item in this function
        tracker = item;



    }




}
