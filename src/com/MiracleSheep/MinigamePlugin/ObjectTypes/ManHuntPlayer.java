/**
 * Description: this class holds players and info about theiur blocks
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

package com.MiracleSheep.MinigamePlugin.ObjectTypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ManHuntPlayer {

    public Player player;

    public int lives;

    public boolean lost_a_life = false;
    public Location disconnect_location;


    public ManHuntPlayer(Player player, int lives) {
        this.player = player;
        this.lives = lives;
    }

}
