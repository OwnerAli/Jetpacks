package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.events.JetpackArmorUnEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JetpackUnEquipListener implements Listener {

    @EventHandler
    public void onUnEquip(JetpackArmorUnEquipEvent event) {
        event.getJetpackPlayer().unEquipCurrentJetpack();
    }

}
