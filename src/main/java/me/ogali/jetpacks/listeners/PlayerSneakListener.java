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
        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> {
                    if (jetpackPlayer.getCurrentJetpack() == null) return;
                    if (jetpackPlayer.getCurrentJetpack().isEnabled() && event.getPlayer().isFlying()) {

                        if (event.getPlayer().isSneaking()) {
                            jetpackPlayer.getCurrentJetpack().triggerHoldShiftAttachments(jetpackPlayer);
                        } else {
                            jetpackPlayer.getCurrentJetpack().triggerClickShiftAttachments(jetpackPlayer);
                        }
                        return;
                    }
                    if (event.getPlayer().isSneaking()) return;
                    jetpackPlayer.getCurrentJetpack().toggle(jetpackPlayer);
                });
    }

}
