package me.smughito.purgeevent.listeners;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        GamePhase currentPhase = plugin.getPhaseManager().getCurrentPhase();

        if (currentPhase != GamePhase.INACTIVE && !player.isOp()) {
            plugin.getPlayerDataManager().setAlive(player.getUniqueId(), true);
            player.setGameMode(GameMode.SURVIVAL);
        }

        if (currentPhase != GamePhase.INACTIVE) {
            plugin.getScoreboardManager().updateScoreboard(player);
        }
    }
}
