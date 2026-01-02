package me.smughito.purgeevent.managers;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class PhaseManager {

    private final PurgeEvent plugin;
    private GamePhase currentPhase;
    private BukkitTask phaseTask;
    private boolean autoPhaseEnabled;

    public PhaseManager(PurgeEvent plugin) {
        this.plugin = plugin;
        this.currentPhase = GamePhase.INACTIVE;
        this.autoPhaseEnabled = false;
    }

    public void startEvent() {
        if (currentPhase != GamePhase.INACTIVE) {
            return;
        }

        plugin.getPlayerDataManager().resetAllData();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOp()) {
                player.setGameMode(GameMode.SURVIVAL);
                plugin.getPlayerDataManager().setAlive(player.getUniqueId(), true);
                plugin.getScoreboardManager().updateScoreboard(player);
            }
        }

        setPhase(GamePhase.PHASE_1);
    }

    public void setPhase(GamePhase phase) {
        currentPhase = phase;
        announcePhase(phase);

        if (phaseTask != null) {
            phaseTask.cancel();
            phaseTask = null;
        }

        if (phase == GamePhase.INACTIVE) {
            endEvent();
        } else if (autoPhaseEnabled) {
            scheduleNextPhase(phase);
        }

        plugin.getScoreboardManager().updateAllScoreboards();
    }

    private void scheduleNextPhase(GamePhase currentPhase) {
        int duration = plugin.getConfig().getInt("phases." + currentPhase.name().toLowerCase() + ".duration", 300) * 20;

        phaseTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (currentPhase == GamePhase.PHASE_1) {
                setPhase(GamePhase.PHASE_2);
            } else if (currentPhase == GamePhase.PHASE_2) {
                setPhase(GamePhase.INACTIVE);
            }
        }, duration);
    }

    private void announcePhase(GamePhase phase) {
        String message = plugin.getConfig().getString("messages.phase-start." + phase.name().toLowerCase(),
            "§6§l" + phase.name() + " has started!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
            player.sendTitle("§6§l" + getPhaseName(phase),
                plugin.getConfig().getString("messages.phase-subtitle." + phase.name().toLowerCase(), ""),
                10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        }
    }

    private String getPhaseName(GamePhase phase) {
        switch (phase) {
            case PHASE_1:
                return "Phase 1";
            case PHASE_2:
                return "Phase 2";
            case INACTIVE:
                return "Event Ended";
            default:
                return phase.name();
        }
    }

    public void endEvent() {
        currentPhase = GamePhase.INACTIVE;

        displayFinalStats();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            plugin.getScoreboardManager().removeScoreboard(player);
        }

        plugin.getPlayerDataManager().resetAllData();
    }

    private void displayFinalStats() {
        PlayerDataManager dataManager = plugin.getPlayerDataManager();

        Bukkit.broadcastMessage("§6§l========== PURGE EVENT ENDED ==========");
        Bukkit.broadcastMessage("");

        Player winner = dataManager.getLastPlayerAlive();
        if (winner != null) {
            Bukkit.broadcastMessage("§e§lWinner: §a" + winner.getName());
        } else {
            Bukkit.broadcastMessage("§e§lNo winner!");
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§6§lTop Killers:");

        dataManager.getTopKillers(3).forEach((uuid, kills) -> {
            Player player = Bukkit.getPlayer(uuid);
            String name = player != null ? player.getName() : "Unknown";
            Bukkit.broadcastMessage("  §e" + name + " §7- §c" + kills + " kills");
        });

        Bukkit.broadcastMessage("§6§l======================================");
    }

    public void checkWinCondition() {
        int aliveCount = plugin.getPlayerDataManager().getAliveCount();

        if (aliveCount <= 1 && currentPhase != GamePhase.INACTIVE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                setPhase(GamePhase.INACTIVE);
            }, 40L);
        }
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setAutoPhaseEnabled(boolean enabled) {
        this.autoPhaseEnabled = enabled;
    }

    public boolean isAutoPhaseEnabled() {
        return autoPhaseEnabled;
    }

    public void cleanup() {
        if (phaseTask != null) {
            phaseTask.cancel();
        }
    }
}
