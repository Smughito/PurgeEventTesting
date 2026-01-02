package me.smughito.purgeevent.managers;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final PurgeEvent plugin;

    public ScoreboardManager(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("purge", "dummy", "§6§lPURGE EVENT");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        GamePhase phase = plugin.getPhaseManager().getCurrentPhase();

        if (phase == GamePhase.INACTIVE) {
            removeScoreboard(player);
            return;
        }

        int line = 10;

        objective.getScore("§7§m------------------").setScore(line--);
        objective.getScore("").setScore(line--);

        objective.getScore("§ePhase: §f" + getPhaseName(phase)).setScore(line--);
        objective.getScore(" ").setScore(line--);

        objective.getScore("§aPlayers Alive: §f" + plugin.getPlayerDataManager().getAliveCount()).setScore(line--);
        objective.getScore("§cYour Kills: §f" + plugin.getPlayerDataManager().getKills(player.getUniqueId())).setScore(line--);

        objective.getScore("  ").setScore(line--);
        objective.getScore("§7§m------------------").setScore(line--);

        player.setScoreboard(scoreboard);
    }

    private String getPhaseName(GamePhase phase) {
        switch (phase) {
            case PHASE_1:
                return "§aPhase 1";
            case PHASE_2:
                return "§cPhase 2";
            case INACTIVE:
                return "§7Inactive";
            default:
                return phase.name();
        }
    }

    public void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
