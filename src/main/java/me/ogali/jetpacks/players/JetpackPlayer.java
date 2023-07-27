package me.ogali.jetpacks.players;

import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JetpackPlayer {

    private final Player player;
    private AbstractJetpack currentJetpack;

    public JetpackPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractJetpack getCurrentJetpack() {
        return currentJetpack;
    }

    public void unEquipCurrentJetpack() {
        currentJetpack.toggle(player);
        currentJetpack = null;
    }

    public void setCurrentJetpack(AbstractJetpack abstractJetpack, ItemStack jetpackItem) {
        if (abstractJetpack instanceof FuelJetpack fuelJetpack) {
            currentJetpack = new FuelJetpack(fuelJetpack);
            currentJetpack.setCurrentFuelLevel(PersistentDataUtils.getFuelLevelFromItem(jetpackItem));
        }
    }

}
