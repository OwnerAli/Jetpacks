package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakListener implements Listener {

    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerSneakListener(JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.getPlayer().isSneaking() || event.getPlayer().isFlying()) return;
        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> {
                    if (jetpackPlayer.getCurrentJetpack() == null) return;
                    jetpackPlayer.getCurrentJetpack().toggle(event.getPlayer());
                });
    }

}
