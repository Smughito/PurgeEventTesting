package me.smughito.purgeevent.commands;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PurgeCommands implements CommandExecutor, TabCompleter {

    private final PurgeEvent plugin;

    public PurgeCommands(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("purge.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                plugin.getPhaseManager().startEvent();
                sender.sendMessage("§aPurge Event started! Phase 1 is now active.");
                break;

            case "stop":
                plugin.getPhaseManager().setPhase(GamePhase.INACTIVE);
                sender.sendMessage("§cPurge Event stopped!");
                break;

            case "phase":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /purge phase <1|2>");
                    return true;
                }
                handlePhaseCommand(sender, args[1]);
                break;

            case "autophase":
                if (args.length < 2) {
                    boolean current = plugin.getPhaseManager().isAutoPhaseEnabled();
                    sender.sendMessage("§eAuto-phase is currently: " + (current ? "§aEnabled" : "§cDisabled"));
                    return true;
                }
                handleAutoPhaseCommand(sender, args[1]);
                break;

            case "reload":
                plugin.reloadConfig();
                sender.sendMessage("§aConfiguration reloaded!");
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void handlePhaseCommand(CommandSender sender, String phaseArg) {
        GamePhase currentPhase = plugin.getPhaseManager().getCurrentPhase();

        if (currentPhase == GamePhase.INACTIVE) {
            sender.sendMessage("§cPlease start the event first with /purge start");
            return;
        }

        switch (phaseArg) {
            case "1":
                plugin.getPhaseManager().setPhase(GamePhase.PHASE_1);
                sender.sendMessage("§aPhase changed to Phase 1!");
                break;
            case "2":
                plugin.getPhaseManager().setPhase(GamePhase.PHASE_2);
                sender.sendMessage("§aPhase changed to Phase 2!");
                break;
            default:
                sender.sendMessage("§cInvalid phase! Use 1 or 2.");
                break;
        }
    }

    private void handleAutoPhaseCommand(CommandSender sender, String arg) {
        switch (arg.toLowerCase()) {
            case "on":
            case "enable":
            case "true":
                plugin.getPhaseManager().setAutoPhaseEnabled(true);
                sender.sendMessage("§aAuto-phase enabled! Phases will progress automatically.");
                break;
            case "off":
            case "disable":
            case "false":
                plugin.getPhaseManager().setAutoPhaseEnabled(false);
                sender.sendMessage("§cAuto-phase disabled! Use /purge phase to manually change phases.");
                break;
            default:
                sender.sendMessage("§cUsage: /purge autophase <on|off>");
                break;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l===== PurgeEvent Commands =====");
        sender.sendMessage("§e/purge start §7- Start the purge event (Phase 1)");
        sender.sendMessage("§e/purge stop §7- Stop the event");
        sender.sendMessage("§e/purge phase <1|2> §7- Manually set the phase");
        sender.sendMessage("§e/purge autophase [on|off] §7- Toggle/view auto-phase");
        sender.sendMessage("§e/purge reload §7- Reload configuration");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("start", "stop", "phase", "autophase", "reload"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("phase")) {
                completions.addAll(Arrays.asList("1", "2"));
            } else if (args[0].equalsIgnoreCase("autophase")) {
                completions.addAll(Arrays.asList("on", "off"));
            }
        }

        return completions;
    }
}
