/**
 * Description: This is the manager class blockhunr
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//name of the package
package com.MiracleSheep.MinigamePlugin.Games;

//importing librairies and otherwise
import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
import com.MiracleSheep.MinigamePlugin.Tasks.RollBlocks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

//this is the manager class
public class BlockHunt extends GameManager {

    //passing the instance of the main class
    public BlockHunt(MinigamePlugin main) {
        super(main);
    }

    //This is an array that will hold the easy blocks
    public static String[] easyBlocks;

    //This is an array that will hold the medium blocks
    public static String[] mediumBlocks;

    //This is an array that will hold the hard blocks
    public static String[] hardBlocks;

    //This is the difficulty
    private int difficulty;


    //function that gets called when the state is inactive - works as a unique clanup functiuon
    @Override
    public void onInactive() {
        roll.stopTimer();
        roll.reset();
    }

    //function that gets called when the state is waiting
    @Override
    public void onWaiting() {
                setGame(1);
                players.add(getStartPlayer());
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: " + getStartPlayer().getDisplayName() + " has started a game of " + getName() + "!");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[Server]: Anyone who wants to play should enter the command /join!");
    }

    //function that gets called when the state is starting
    @Override
    public void onStarting() {


        for (int i = 0; i < getMain().getConfig().getStringList("BlocksRolled" + ".Easy").size(); i++) {
            easyBlocks[i] = getMain().getConfig().getStringList("BlocksRolled" + ".Easy").get(i);
        }

        for (int i = 0; i < getMain().getConfig().getStringList("BlocksRolled" + ".Medium").size(); i++) {
            mediumBlocks[i] = getMain().getConfig().getStringList("BlocksRolled" + ".Medium").get(i);
        }

        for (int i = 0; i < getMain().getConfig().getStringList("BlocksRolled" + ".Hard").size(); i++) {
            hardBlocks[i] = getMain().getConfig().getStringList("BlocksRolled" + ".Hard").get(i);
        }

        difficulty = getMain().getConfig().getInt("BlockHuntDifficulty");

        setState(GameState.ACTIVE);


    }

    //function that gets called when the state is active
    @Override
    public void onActive() {


    }

    //function that gets called when the state is active
    @Override
    public void onTransition() {

    }

    //function that gets called when the state is won
    @Override
    public void onWon() {

    }















}
