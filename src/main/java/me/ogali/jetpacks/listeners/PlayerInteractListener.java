package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.ItemTypeUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final JetpackRegistry jetpackRegistry;
    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerInteractListener(JetpackRegistry jetpackRegistry, JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackRegistry = jetpackRegistry;
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!Action.RIGHT_CLICK_AIR.equals(event.getAction()) &&
                !Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) return;
        if (event.getItem() == null) return;
        if (!ItemTypeUtils.isArmor(event.getItem().getType())) return;

        jetpackPlayerRegistry.getJetpackPlayerByPlayer(event.getPlayer())
                .ifPresent(jetpackPlayer -> {
                    jetpackPlayer.unEquipCurrentJetpack();
                    jetpackRegistry.getJetpackByItemStack(event.getItem())
                            .ifPresent(abstractJetpack -> {
                                jetpackPlayer.setCurrentJetpack(abstractJetpack, event.getItem());
                                jetpackPlayer.setJetpackItem(event.getItem());
                            });
                });
    }

}
