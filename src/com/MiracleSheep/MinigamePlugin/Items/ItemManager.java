/**
 * Description: This is a class responsible for creating any extra items that the plugin may have. This class is optional.
 *
 * @author: John Khalife
 * @version: Created August 8th 2021
 */


//Name of the package
package com.MiracleSheep.MinigamePlugin.Items;


//importing libraries and packages
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

//This is the class that create items for the game that can be crafted or summoned with commands (optionnal)
public class ItemManager {

    //creating an item called testitem
    public static ItemStack testitem;
    //creating an instance of the main class
    public static MinigamePlugin main;

    //This passes information to the main class
    public ItemManager (MinigamePlugin main) {
        this.main = main;

    }




    //this function initializes all the items needed by calling their create functions
    public static void init() {
        createItem();
    }



    //This method is responsable for creating a single item it creates a nether star in this example
    private static void createItem() {
        //creating the item and what it appears as and amount
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        //modifying the meta of the item
        ItemMeta meta = item.getItemMeta();
        //modifying display name
        meta.setDisplayName("§4DisplayName");
        //modifying lore
        List<String> lore = new ArrayList<>();
        lore.add("§bLore");
        meta.setLore(lore);
        //adding enchantments
        meta.addEnchant(Enchantment.LUCK, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        //setting the meta to this item
        item.setItemMeta(meta);
        //setting the class variable testitem to the item in this function
        testitem = item;

        //Example of a shared recipe - a recipe with a defined shape in a crafting table
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("item"),item);
        sr.shape("III","IDI","III");
        sr.setIngredient('I',Material.IRON_INGOT);
        sr.setIngredient('D', Material.DIAMOND);
        Bukkit.getServer().addRecipe(sr);

    //Example of a shapeless recipe - recipe without much shape
    ShapelessRecipe s = new ShapelessRecipe(NamespacedKey.minecraft("shapeless_item"), item);
        s.addIngredient(2, Material.BLAZE_POWDER);
        Bukkit.getServer().addRecipe(s);

    //example of a furnace recipe
    FurnaceRecipe smelt = new FurnaceRecipe(NamespacedKey.minecraft("item_smelt"), item , Material.EMERALD , 1.0f,1*20);
        Bukkit.getServer().addRecipe(smelt);


    }

}
