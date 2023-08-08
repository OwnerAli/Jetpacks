package me.ogali.jetpacks.events;

import me.ogali.jetpacks.players.JetpackPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class JetpackArmorUnEquipEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final JetpackPlayer jetpackPlayer;

    public JetpackArmorUnEquipEvent(@NotNull JetpackPlayer jetpackPlayer) {
        this.jetpackPlayer = jetpackPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public JetpackPlayer getJetpackPlayer() {
        return jetpackPlayer;
    }

}
