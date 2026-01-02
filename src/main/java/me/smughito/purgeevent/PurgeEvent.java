package me.smughito.purgeevent;

import me.smughito.purgeevent.commands.PurgeCommands;
import me.smughito.purgeevent.listeners.PlayerDeathListener;
import me.smughito.purgeevent.listeners.PlayerJoinListener;
import me.smughito.purgeevent.listeners.PlayerQuitListener;
import me.smughito.purgeevent.listeners.PvpListener;
import me.smughito.purgeevent.managers.PhaseManager;
import me.smughito.purgeevent.managers.ScoreboardManager;
import me.smughito.purgeevent.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PurgeEvent extends JavaPlugin {

    private static PurgeEvent instance;
    private PhaseManager phaseManager;
    private ScoreboardManager scoreboardManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        playerDataManager = new PlayerDataManager();
        phaseManager = new PhaseManager(this);
        scoreboardManager = new ScoreboardManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("PurgeEvent has been enabled!");
    }

    @Override
    public void onDisable() {
        if (phaseManager != null) {
            phaseManager.cleanup();
        }
        getLogger().info("PurgeEvent has been disabled!");
    }

    private void registerCommands() {
        PurgeCommands commands = new PurgeCommands(this);
        getCommand("purge").setExecutor(commands);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);
    }

    public static PurgeEvent getInstance() {
        return instance;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
