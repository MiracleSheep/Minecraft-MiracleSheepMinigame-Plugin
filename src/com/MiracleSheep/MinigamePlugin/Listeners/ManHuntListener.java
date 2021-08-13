/**
 * Description: this class is responsable for listening to events from the Man Hunt minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//This is the name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//These are the required librairies and packages
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.event.Listener;

//this is the class that listens for hunt events
public class ManHuntListener implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;


    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public ManHuntListener(MinigamePlugin main) {
        this.main = main;
    }

}

