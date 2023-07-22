package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.domain.JetpackRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FuelBurnRunnable extends JetpackRunnable {

    private final AbstractJetpack jetpack;

    public FuelBurnRunnable(AbstractJetpack abstractJetpack) {
        super(abstractJetpack);
        this.jetpack = abstractJetpack;
    }

    @Override
    public void start() {
        long burnRateInSeconds = getAbstractJetpack().getFuelBurnRateInSeconds() * 20;
        setBukkitTask(runTaskTimer(JetpackPlugin.getInstance(), 0, burnRateInSeconds));
    }

    @Override
    public void stop() {
        getBukkitTask().cancel();
    }

    @Override
    public void run() {
        if (!jetpack.isEnabled() || jetpack.getCurrentFuelAmount() - jetpack.getFuelBurnAmountPerBurnRate() <= 0) {
            jetpack.setCurrentFuelAmount(0);
            stop();
            return;
        }
        int newFuelAmount = jetpack.getCurrentFuelAmount() - jetpack.getFuelBurnAmountPerBurnRate();
        jetpack.setCurrentFuelAmount(newFuelAmount);
    }

}
