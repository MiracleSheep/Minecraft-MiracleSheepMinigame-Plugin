/**
 * Description: this class holds players and info about theiur blocks
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

package com.MiracleSheep.MinigamePlugin.Tasks;

import org.bukkit.entity.Player;

public class BlockHuntPlayer {

    public Player player;

    public String block;

    public BlockHuntPlayer(Player player, String block) {
        this.player = player;
        this.block = block;
    }

}
