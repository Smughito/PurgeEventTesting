package me.smughito.purgeevent.commands;

import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import me.smughito.purgeevent.utils.GradientUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PurgeCommands implements CommandExecutor, TabCompleter {

    private final PurgeEvent plugin;

    public PurgeCommands(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("purge.admin")) {
            sender.sendMessage(GradientUtil.applyColor("<#FF5555>You don't have permission to use this command!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "start":
                plugin.getPhaseManager().startEvent();
                sender.sendMessage(GradientUtil.applyColor("<gradient:#00FF00:#00AA00>Purge Event started!</gradient>"));
                break;

            case "stop":
                plugin.getPhaseManager().endEvent();
                sender.sendMessage(GradientUtil.applyColor("<gradient:#FF0000:#AA0000>Purge Event stopped!</gradient>"));
                break;

            case "phase":
                if (args.length < 2) {
                    sender.sendMessage(GradientUtil.applyColor("<#FF5555>Usage: /purge phase <1|2>"));
                    return true;
                }

                try {
                    int phaseNum = Integer.parseInt(args[1]);
                    GamePhase phase = phaseNum == 1 ? GamePhase.PHASE_1 : GamePhase.PHASE_2;
                    plugin.getPhaseManager().setPhase(phase);
                    sender.sendMessage(GradientUtil.applyColor("<gradient:#FFD700:#FFA500>Phase changed to Phase " + phaseNum + "!</gradient>"));
                } catch (NumberFormatException e) {
                    sender.sendMessage(GradientUtil.applyColor("<#FF5555>Invalid phase number!"));
                }
                break;

            case "autophase":
                boolean newState;

                if (args.length < 2) {
                    newState = !plugin.getPhaseManager().isAutoPhaseEnabled();
                } else {
                    newState = args[1].equalsIgnoreCase("on");
                }

                plugin.getPhaseManager().setAutoPhaseEnabled(newState);
                String state = newState ? "enabled" : "disabled";
                sender.sendMessage(GradientUtil.applyColor("<gradient:#FFD700:#FFA500>Auto-phase " + state + "!</gradient>"));
                break;

            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(GradientUtil.applyColor("<gradient:#00FF00:#00AA00>Configuration reloaded!</gradient>"));
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(GradientUtil.applyColor("<gradient:#FFD700:#FFA500>==================== Purge Event Commands ====================</gradient>"));
        sender.sendMessage(GradientUtil.applyColor("<#FFAA00>/purge start <#FFFFFF>- Start the Purge Event"));
        sender.sendMessage(GradientUtil.applyColor("<#FFAA00>/purge stop <#FFFFFF>- Stop the Purge Event"));
        sender.sendMessage(GradientUtil.applyColor("<#FFAA00>/purge phase <1|2> <#FFFFFF>- Change the phase"));
        sender.sendMessage(GradientUtil.applyColor("<#FFAA00>/purge autophase [on|off] <#FFFFFF>- Toggle auto-phase progression"));
        sender.sendMessage(GradientUtil.applyColor("<#FFAA00>/purge reload <#FFFFFF>- Reload configuration"));
        sender.sendMessage(GradientUtil.applyColor("<gradient:#FFD700:#FFA500>================================================================</gradient>"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("phase");
            completions.add("autophase");
            completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("phase")) {
            completions.add("1");
            completions.add("2");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("autophase")) {
            completions.add("on");
            completions.add("off");
        }

        return completions;
    }
}
