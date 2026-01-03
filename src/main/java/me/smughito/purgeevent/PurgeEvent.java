package me.smughito.purgeevent;

import me.smughito.purgeevent.commands.PurgeCommands;
import me.smughito.purgeevent.commands.SpectatorChatCommand;
import me.smughito.purgeevent.listeners.PlayerDeathListener;
import me.smughito.purgeevent.listeners.PlayerJoinListener;
import me.smughito.purgeevent.listeners.PlayerQuitListener;
import me.smughito.purgeevent.listeners.PvpListener;
import me.smughito.purgeevent.listeners.SpectatorChatListener;
import me.smughito.purgeevent.managers.PhaseManager;
import me.smughito.purgeevent.managers.PlayerDataManager;
import me.smughito.purgeevent.managers.ScoreboardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PurgeEvent extends JavaPlugin {

    private PhaseManager phaseManager;
    private PlayerDataManager playerDataManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        playerDataManager = new PlayerDataManager();
        phaseManager = new PhaseManager(this);
        scoreboardManager = new ScoreboardManager(this);

        getCommand("purge").setExecutor(new PurgeCommands(this));
        getCommand("sc").setExecutor(new SpectatorChatCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);
        getServer().getPluginManager().registerEvents(new SpectatorChatListener(this), this);

        getLogger().info("PurgeEvent plugin enabled!");
    }

    @Override
    public void onDisable() {
        scoreboardManager.stopUpdating();
        scoreboardManager.removeAllScoreboards();
        getLogger().info("PurgeEvent plugin disabled!");
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
