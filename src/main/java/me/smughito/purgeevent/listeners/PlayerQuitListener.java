package me.smughito.purgeevent.listeners;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PurgeEvent plugin;

    public PlayerQuitListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePhase currentPhase = plugin.getPhaseManager().getCurrentPhase();

        if (currentPhase != GamePhase.INACTIVE && !player.isOp()) {
            plugin.getPlayerDataManager().setAlive(player.getUniqueId(), false);
            plugin.getPhaseManager().checkWinCondition();
            plugin.getScoreboardManager().updateAllScoreboards();
        }
    }
}
