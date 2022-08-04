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
        meta.addEnchant(Enchantment.LUCK, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //this part of the code checks all the locations of the runners and the hunter requesting, and then gets the closest player
        //setting the initial location to the first player in the runner list

        Location closestLocation = manhunt.runners.get(0).player.getLocation();
        Location hunterLocation = hunter.getLocation();
        Player closestPlayer = manhunt.runners.get(0).player;
        double closestDist;

        for (int i = 0; i < manhunt.runners.size(); i++)  {
            closestDist = closestLocation.distance(hunterLocation);

            if (manhunt.runners.get(i).player.getLocation().distance(hunterLocation) < closestDist) {
                closestDist = manhunt.runners.get(i).player.getLocation().distance(hunterLocation);
                closestPlayer =  manhunt.runners.get(i).player;
                closestLocation = manhunt.runners.get(i).player.getLocation();

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

        if (manhunt.limit == 2) {
            if (closestLocation.distance(hunterLocation) > main.getConfig().getInt("ManhuntTeleportDistance")) {

                if (hunter.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    hunter.teleport(closestLocation.add(main.getConfig().getInt("ManhuntTeleportDistance"),0,0));
                    boolean issafe = false;
                    Location loc = hunter.getLocation();
                    while (!issafe) {

                    for (int j = 3 ; j < 125 ; j++) {

                        if (!issafe) {
                            loc.setY(j);
                            if (loc.subtract(0, 1, 0).getBlock().getType().isSolid() && loc.getBlock().getType().isAir() && loc.add(0,1,0).getBlock().getType().isAir()) {
                                issafe = true;
                            }



                        }

                    }
                        if (!issafe) {
                            loc.add(1,0,0);
                        }

                    }
                    hunter.teleport(loc);

                } else {
                hunter.teleport(closestLocation.add(main.getConfig().getInt("ManhuntTeleportDistance"),0,0));
                Location loc = hunter.getLocation();
                loc.setY(hunter.getWorld().getHighestBlockAt( hunter.getLocation().getBlockX(), hunter.getLocation().getBlockZ()).getY() + 1);
                hunter.teleport(loc);
                }
            }
        }



    }




}
