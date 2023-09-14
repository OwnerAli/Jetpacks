package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerLeaveListener(JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        jetpackPlayerRegistry.unregisterJetpackPlayerByPlayer(event.getPlayer());
    }

}
