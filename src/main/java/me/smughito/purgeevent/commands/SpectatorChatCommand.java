package me.smughito.purgeevent.commands;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.utils.GradientUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorChatCommand implements CommandExecutor {

    private final PurgeEvent plugin;

    public SpectatorChatCommand(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getPlayerDataManager().isSpectator(player)) {
            player.sendMessage(GradientUtil.applyColor("<#FF5555>You must be a spectator to use this command!"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(GradientUtil.applyColor("<#FFAA00>Usage: /sc <message>"));
            return true;
        }

        String message = String.join(" ", args);
        String format = plugin.getConfig().getString("spectator.spectator-chat-format",
            "<#666666>[SPEC] %player%: <#999999>%message%");

        String spectatorMessage = format
            .replace("%player%", player.getName())
            .replace("%message%", message);

        spectatorMessage = GradientUtil.applyColor(spectatorMessage);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (plugin.getPlayerDataManager().isSpectator(onlinePlayer)) {
                onlinePlayer.sendMessage(spectatorMessage);
            }
        }

        return true;
    }
}
