package me.smughito.purgeevent.listeners;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PurgeEvent plugin;

    public PlayerJoinListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (plugin.getPhaseManager().getCurrentPhase() != GamePhase.INACTIVE && !player.isOp()) {
            player.setGameMode(GameMode.SURVIVAL);
            plugin.getPlayerDataManager().setAlive(player);
        }

        plugin.getScoreboardManager().updateScoreboard(player);
    }
}
