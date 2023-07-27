package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.domain.JetpackRunnable;
import org.bukkit.entity.Player;

public class FuelBurnRunnable extends JetpackRunnable {

    private final AbstractJetpack jetpack;
    private final Player player;

    public FuelBurnRunnable(AbstractJetpack abstractJetpack, Player player) {
        super(abstractJetpack);
        this.jetpack = abstractJetpack;
        this.player = player;
    }

    @Override
    public void start() {
        long burnRateInSeconds = getAbstractJetpack().getFuelBurnRateInSeconds() * 20;
        setBukkitTask(runTaskTimer(JetpackPlugin.getInstance(), 0, burnRateInSeconds));
    }

    @Override
    public void run() {
        if (!jetpack.isEnabled() || jetpack.getCurrentFuelLevel() - jetpack.getFuelBurnAmountPerBurnRate() <= 0) {
            jetpack.setCurrentFuelLevel(0);
            jetpack.toggle(player);
            stop();
            return;
        }
        double newFuelAmount = jetpack.getCurrentFuelLevel() - jetpack.getFuelBurnAmountPerBurnRate();
        jetpack.setCurrentFuelLevel(newFuelAmount);
    }

}
