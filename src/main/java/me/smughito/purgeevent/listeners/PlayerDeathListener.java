package me.smughito.purgeevent.listeners;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import me.smughito.purgeevent.utils.DeathMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final PurgeEvent plugin;

    public PlayerDeathListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        GamePhase currentPhase = plugin.getPhaseManager().getCurrentPhase();

        if (currentPhase == GamePhase.INACTIVE) {
            return;
        }

        event.setDeathMessage(null);

        if (killer != null && !killer.isOp()) {
            plugin.getPlayerDataManager().addKill(killer.getUniqueId());
            plugin.getScoreboardManager().updateScoreboard(killer);
        }

        if (!victim.isOp()) {
            plugin.getPlayerDataManager().setAlive(victim.getUniqueId(), false);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                victim.spigot().respawn();
                victim.setGameMode(GameMode.SPECTATOR);
            }, 1L);

            String deathMessage = DeathMessageUtil.getCustomDeathMessage(plugin, victim, killer);
            Bukkit.broadcastMessage(deathMessage);

            plugin.getPhaseManager().checkWinCondition();
        }

        plugin.getScoreboardManager().updateAllScoreboards();
    }
}
