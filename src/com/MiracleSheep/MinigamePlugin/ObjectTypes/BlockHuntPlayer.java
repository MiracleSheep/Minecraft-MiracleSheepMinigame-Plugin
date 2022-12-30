/**
 * Description: this class holds players and info about theiur blocks
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

package com.MiracleSheep.MinigamePlugin.ObjectTypes;

import org.bukkit.entity.Player;

public class BlockHuntPlayer {

    public Player player;

    public String block;

    public int lives;

    public boolean found = false;

    public BlockHuntPlayer(Player player, String block, int lives) {
        this.player = player;
        this.block = block;
        this.lives = lives;
    }

}
