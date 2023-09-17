package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.fuels.domain.FuelHolder;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.animations.ArmorStandMover;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class JetpackTuningAnimationRunnable extends ArmorStandMover {

    private final Player player;
    private final ArmorStand armorStand;
    private final AbstractJetpack abstractJetpack;
    private final FuelHolder fuelHolder;
    private final Location targetLocation;

    public JetpackTuningAnimationRunnable(Player player, ArmorStand armorStand, AbstractJetpack abstractJetpack, FuelHolder fuelHolder,
                                          Location targetLocation, int durationSeconds) {
        super(armorStand, targetLocation, durationSeconds);
        this.player = player;
        this.armorStand = armorStand;
        this.abstractJetpack = abstractJetpack;
        this.fuelHolder = fuelHolder;
        this.targetLocation = targetLocation;
    }

    @Override
    public void run() {
        super.run();
        if (armorStand.getLocation().distance(targetLocation) <= 0.1) {
            this.cancel();
            armorStand.remove();
        }
    }

    private void runTuningAnimation() {
        // TODO: Add animation for jetpack tuning after adding logic in jetpack player class
        return;
    }

}
