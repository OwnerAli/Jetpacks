package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
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
        if (event.getPlayer().isSneaking()) return;
        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> {
                    AbstractJetpack currentJetpack = jetpackPlayer.getCurrentJetpack();
                    if (currentJetpack == null) return;
                    if (currentJetpack.isEnabled() && event.getPlayer().isFlying()) {
                        currentJetpack.triggerClickShiftAttachments(jetpackPlayer);
                        return;
                    }
                    currentJetpack.toggle(jetpackPlayer);
                });
    }

}
