package me.smughito.purgeevent.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.smughito.purgeevent.PurgeEvent;
import me.smughito.purgeevent.phases.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PvpListener implements Listener {

    private final PurgeEvent plugin;

    public PvpListener(PurgeEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        GamePhase currentPhase = plugin.getPhaseManager().getCurrentPhase();

        if (currentPhase != GamePhase.PHASE_1) {
            return;
        }

        if (isInPvpRegion(victim)) {
            return;
        }

        event.setCancelled(true);
        attacker.sendMessage(plugin.getConfig().getString("messages.pvp-disabled",
            "§cPvP is disabled outside of designated regions in Phase 1!"));
    }

    private boolean isInPvpRegion(Player player) {
        try {
            List<String> pvpRegions = plugin.getConfig().getStringList("phases.phase_1.pvp-regions");

            if (pvpRegions.isEmpty()) {
                return false;
            }

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

            for (ProtectedRegion region : set) {
                if (pvpRegions.contains(region.getId())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
