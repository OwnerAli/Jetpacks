package me.ogali.jetpacks.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.ogali.jetpacks.JetpackPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private final JetpackPlugin jetpackPlugin;

    public PlayerDropItemListener(JetpackPlugin jetpackPlugin) {
        this.jetpackPlugin = jetpackPlugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!jetpackPlugin.getJetpackRegistry().isJetpackItem(event.getItemDrop().getItemStack())) return;

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(event.getPlayer().getWorld()));

        if (regionManager == null) return;
        if (regionManager.getApplicableRegions(BlockVector3.at(event.getPlayer().getLocation().getBlockX(),
                event.getPlayer().getLocation().getBlockY(), event.getPlayer().getLocation().getBlockZ())).size() == 0)
            return;
        regionManager.getApplicableRegions(BlockVector3.at(event.getPlayer().getLocation().getBlockX(),
                        event.getPlayer().getLocation().getBlockY(), event.getPlayer().getLocation().getBlockZ()))
                .forEach(protectedRegion -> {
                    BooleanFlag fuelExtractorFlag = jetpackPlugin.getFuelExtractorExecutableRegion().getFuelExtractorFlag();
                    BooleanFlag jetpackTuningFlag = jetpackPlugin.getJetpackTunerExecutableRegion().getJetpackTuningFlag();

                    if (Boolean.TRUE.equals(protectedRegion.getFlag(fuelExtractorFlag))) {
                        jetpackPlugin.getFuelExtractorExecutableRegion().execute(protectedRegion, event);
                    } else if (Boolean.TRUE.equals(protectedRegion.getFlag(jetpackTuningFlag))) {
                        jetpackPlugin.getJetpackTunerExecutableRegion().execute(protectedRegion, event);
                    }
                });
    }

}
