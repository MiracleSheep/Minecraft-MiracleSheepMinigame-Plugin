/**
 * Description: this class is responsable for listening to events from the ManSwap minigame
 *
 * @author: John Khalife
 * @version: Created August 10th 2021
 */

//This is the name of the package
package com.MiracleSheep.MinigamePlugin.Listeners;

//These are the required librairies and packages
        import com.MiracleSheep.MinigamePlugin.Games.GameState;
        import com.MiracleSheep.MinigamePlugin.Games.DeathSwap;
        import com.MiracleSheep.MinigamePlugin.MinigamePlugin;
        import org.bukkit.Bukkit;
        import org.bukkit.ChatColor;
        import org.bukkit.entity.Player;
        import org.bukkit.event.EventHandler;
        import org.bukkit.event.Listener;
        import org.bukkit.event.entity.EntityDamageByEntityEvent;
        import org.bukkit.event.entity.PlayerDeathEvent;

//this is the class that listens for block hunt events
public class ManSwapListener implements Listener {

    //getting an instance of the main class
    public static MinigamePlugin main;


    //This is the constructor for the events class. it passes the inventory (optionnal) and main classes
    public ManSwapListener(MinigamePlugin main) {
        this.main = main;
    }




    //THis event detects when a player is hit
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        DeathSwap manswap = new DeathSwap(main);

        if (!(e.getEntity() instanceof Player)) {return;}



        Player player = (Player) e.getEntity();

        if (e.getDamager() instanceof Player) {



        if (manswap.getGame() == 3) {

            if (manswap.players.contains(player)) {

                if (manswap.getGameState() == GameState.ACTIVE) {
                        e.setCancelled(true);

                }

            }

        }
        }
    }


}

