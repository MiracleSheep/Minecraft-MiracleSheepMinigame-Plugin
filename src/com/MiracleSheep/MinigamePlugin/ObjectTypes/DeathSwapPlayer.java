package com.MiracleSheep.MinigamePlugin.ObjectTypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DeathSwapPlayer {

    public Player player;

    public int lives;

    public Location spawnpoint;

    public DeathSwapPlayer(Player player, int lives) {
        this.player = player;
        this.lives = lives;
    }

}
