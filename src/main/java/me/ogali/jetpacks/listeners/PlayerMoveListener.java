package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.attatchments.domain.impl.MoveAttachmentAction;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerMoveListener(JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getX() <= event.getFrom().getX() + 1
                && event.getTo().getY() <= event.getFrom().getY() + 1
                && event.getTo().getZ() <= event.getFrom().getZ() + 1) return;
        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> {
                    AbstractJetpack currentJetpack = jetpackPlayer.getCurrentJetpack();

                    if (currentJetpack == null) return;
                    if (currentJetpack.getAttachmentList().isEmpty()) return;

                    currentJetpack.getAttachmentList()
                            .forEach(attachment -> {
                                if (attachment.getTriggerAction() instanceof MoveAttachmentAction) {
                                    attachment.getTriggerAction().trigger(jetpackPlayer);
                                } else if (attachment.getCancellationAction() instanceof MoveAttachmentAction) {
                                    attachment.getCancellationAction().trigger(jetpackPlayer);
                                }
                            });
                });
    }

}
