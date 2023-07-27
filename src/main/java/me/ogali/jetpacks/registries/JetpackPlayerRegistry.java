package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.players.JetpackPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JetpackPlayerRegistry {

    private final Set<JetpackPlayer> jetpackPlayerSet = new HashSet<>();

    public void registerJetpackPlayer(JetpackPlayer jetpackPlayer) {
        jetpackPlayerSet.add(jetpackPlayer);
    }

    public void unregisterJetpackPlayerByPlayer(Player player) {
        jetpackPlayerSet.removeIf(jetpackPlayer -> jetpackPlayer.getPlayer().equals(player));
    }

    public Optional<JetpackPlayer> getJetpackPlayerByPlayer(Player player) {
        return jetpackPlayerSet.stream()
                .filter(jetpackPlayer -> jetpackPlayer.getPlayer().equals(player))
                .findFirst();
    }

}
