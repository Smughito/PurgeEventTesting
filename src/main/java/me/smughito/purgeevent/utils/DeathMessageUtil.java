package me.smughito.purgeevent.utils;

import me.smughito.purgeevent.PurgeEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class DeathMessageUtil {

    private static final Random random = new Random();

    public static String getCustomDeathMessage(PurgeEvent plugin, Player victim, Player killer) {
        FileConfiguration config = plugin.getConfig();

        if (killer != null) {
            List<String> messages = config.getStringList("death-messages.player-kill");
            if (!messages.isEmpty()) {
                String template = messages.get(random.nextInt(messages.size()));
                return template
                    .replace("{victim}", victim.getName())
                    .replace("{killer}", killer.getName())
                    .replace("&", "§");
            }
            return "§c" + victim.getName() + " §7was eliminated by §c" + killer.getName();
        } else {
            List<String> messages = config.getStringList("death-messages.natural-death");
            if (!messages.isEmpty()) {
                String template = messages.get(random.nextInt(messages.size()));
                return template
                    .replace("{victim}", victim.getName())
                    .replace("&", "§");
            }
            return "§c" + victim.getName() + " §7has been eliminated";
        }
    }
}
