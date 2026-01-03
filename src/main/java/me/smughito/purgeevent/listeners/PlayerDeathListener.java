package me.smughito.purgeevent.listeners;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import me.smughito.purgeevent.utils.GradientUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class PlayerDeathListener implements Listener {

    private final PurgeEvent plugin;
    private final Random random = new Random();

    public PlayerDeathListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (plugin.getPhaseManager().getCurrentPhase() == GamePhase.INACTIVE) {
            return;
        }

        event.setDeathMessage(null);

        Player killer = victim.getKiller();

        if (killer != null && !killer.isOp()) {
            plugin.getPlayerDataManager().incrementKills(killer);
        }

        if (!victim.isOp()) {
            plugin.getPlayerDataManager().setDead(victim);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                victim.setGameMode(GameMode.SPECTATOR);
            }, 1L);
        }

        String deathMessage;

        if (killer != null && !killer.isOp()) {
            List<String> playerKilledMessages = plugin.getConfig().getStringList("death.messages.player-killed");
            deathMessage = playerKilledMessages.get(random.nextInt(playerKilledMessages.size()));
            deathMessage = deathMessage
                .replace("%victim%", victim.getName())
                .replace("%killer%", killer.getName());
        } else {
            List<String> naturalDeathMessages = plugin.getConfig().getStringList("death.messages.natural-death");
            deathMessage = naturalDeathMessages.get(random.nextInt(naturalDeathMessages.size()));
            deathMessage = deathMessage.replace("%victim%", victim.getName());
        }

        deathMessage = GradientUtil.applyColor(deathMessage);
        Bukkit.broadcastMessage(deathMessage);

        plugin.getPhaseManager().checkWinCondition();
        plugin.getScoreboardManager().updateAllScoreboards();
    }
}
