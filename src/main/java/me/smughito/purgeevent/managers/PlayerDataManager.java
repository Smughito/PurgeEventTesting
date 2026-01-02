package me.smughito.purgeevent.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerDataManager {

    private final Map<UUID, Integer> kills;
    private final Set<UUID> alivePlayers;

    public PlayerDataManager() {
        this.kills = new HashMap<>();
        this.alivePlayers = new HashSet<>();
    }

    public void addKill(UUID playerUuid) {
        kills.put(playerUuid, kills.getOrDefault(playerUuid, 0) + 1);
    }

    public int getKills(UUID playerUuid) {
        return kills.getOrDefault(playerUuid, 0);
    }

    public void setAlive(UUID playerUuid, boolean alive) {
        if (alive) {
            alivePlayers.add(playerUuid);
        } else {
            alivePlayers.remove(playerUuid);
        }
    }

    public boolean isAlive(UUID playerUuid) {
        return alivePlayers.contains(playerUuid);
    }

    public int getAliveCount() {
        return (int) alivePlayers.stream()
            .filter(uuid -> Bukkit.getPlayer(uuid) != null && !Bukkit.getPlayer(uuid).isOp())
            .count();
    }

    public Player getLastPlayerAlive() {
        if (getAliveCount() == 1) {
            UUID lastUuid = alivePlayers.stream()
                .filter(uuid -> {
                    Player p = Bukkit.getPlayer(uuid);
                    return p != null && !p.isOp();
                })
                .findFirst()
                .orElse(null);

            if (lastUuid != null) {
                return Bukkit.getPlayer(lastUuid);
            }
        }
        return null;
    }

    public LinkedHashMap<UUID, Integer> getTopKillers(int limit) {
        return kills.entrySet().stream()
            .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    public void resetAllData() {
        kills.clear();
        alivePlayers.clear();
    }

    public void resetPlayer(UUID playerUuid) {
        kills.remove(playerUuid);
        alivePlayers.remove(playerUuid);
    }
}
