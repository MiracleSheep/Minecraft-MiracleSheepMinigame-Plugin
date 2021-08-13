/**
 * Description: This is a manager class for the Man Swap minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


//this is the manager class
public class ManSwap extends GameManager {

    //passing the instance of the main class
    public ManSwap(MinigamePlugin main) {
        super(main);
    }

    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {

    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
        setGame(3);
        players.add(getStartPlayer());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + " has started a game of " + getName() + "!");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");

    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {

    }

    //function that gets called when the state is active
    @Override
    public void onActive() {

    }

    //function that gets called when the state is transition
    @Override
    public void onTransition() {

    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }
















}
