package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.players.JetpackPlayer;
import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final JetpackPlayerRegistry jetpackPlayerRegistry;

    public PlayerJoinListener(JetpackPlayerRegistry jetpackPlayerRegistry) {
        this.jetpackPlayerRegistry = jetpackPlayerRegistry;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        jetpackPlayerRegistry.registerJetpackPlayer(new JetpackPlayer(event.getPlayer()));
    }

}
