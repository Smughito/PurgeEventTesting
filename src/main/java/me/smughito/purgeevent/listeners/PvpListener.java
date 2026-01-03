package me.smughito.purgeevent.listeners;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import me.smughito.purgeevent.utils.GradientUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    private final PurgeEvent plugin;

    public PvpListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (plugin.getPhaseManager().getCurrentPhase() != GamePhase.PHASE_1) {
            return;
        }

        if (!isInPvpRegion(attacker)) {
            event.setCancelled(true);
            String message = plugin.getConfig().getString("pvp.phase1-restriction-message",
                "<#FF5555>PvP is disabled outside designated zones during Phase 1!");
            attacker.sendMessage(GradientUtil.applyColor(message));
        }
    }

    private boolean isInPvpRegion(Player player) {
        try {
            var regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            var worldData = regionContainer.get(player.getWorld());

            if (worldData == null) {
                return false;
            }

            ApplicableRegionSet regions = worldData.getApplicableRegions(
                com.sk89q.worldedit.util.Location.create(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ()
                )
            );

            for (String regionName : plugin.getConfig().getStringList("phases.phase-1.pvp-regions")) {
                ProtectedRegion region = worldData.getRegion(regionName);
                if (region != null && regions.getRegions().contains(region)) {
                    return true;
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error checking PvP regions: " + e.getMessage());
        }

        return false;
    }
}
