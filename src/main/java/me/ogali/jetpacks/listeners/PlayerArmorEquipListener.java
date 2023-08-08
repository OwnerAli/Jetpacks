package me.ogali.jetpacks.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.registries.JetpackRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerArmorEquipListener implements Listener {

    private final JetpackRegistry jetpackRegistry;

    public PlayerArmorEquipListener(JetpackRegistry jetpackRegistry) {
        this.jetpackRegistry = jetpackRegistry;
    }

    @EventHandler
    public void onArmorEquip(PlayerArmorChangeEvent event) {
        jetpackRegistry.getJetpackByItemStack(event.getNewItem())
                .ifPresent(abstractJetpack -> JetpackPlugin.getInstance().getJetpackPlayerRegistry()
                        .getJetpackPlayerByPlayer(event.getPlayer())
                        .ifPresent(jetpackPlayer -> {
                            jetpackPlayer.setCurrentJetpack(abstractJetpack, event.getNewItem());
                            jetpackPlayer.setJetpackItem(event.getPlayer().getInventory().getItem(38));
                        }));
    }

}
