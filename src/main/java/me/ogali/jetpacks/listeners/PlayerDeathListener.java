package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.events.JetpackArmorUnEquipEvent;
import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerDeathListener(JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> Bukkit.getPluginManager().callEvent(new JetpackArmorUnEquipEvent(jetpackPlayer)));
    }

}
