/**
 * Description: this class is responsable for listening to events from the ManSwap minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//This is the name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//These are the required librairies and packages
        import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
        import org.bukkit.event.Listener;

//this is the class that listens for block hunt events
public class ManSwapListener implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;


    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public ManSwapListener(MinigamePlugin main) {
        this.main = main;
    }

}

