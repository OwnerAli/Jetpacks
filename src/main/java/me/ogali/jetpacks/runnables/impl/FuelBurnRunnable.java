package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.players.JetpackPlayer;
import me.ogali.jetpacks.runnables.domain.JetpackRunnable;

public class FuelBurnRunnable extends JetpackRunnable {

    private final AbstractJetpack jetpack;
    private final JetpackPlayer jetpackPlayer;

    public FuelBurnRunnable(AbstractJetpack abstractJetpack, JetpackPlayer jetpackPlayer) {
        super(abstractJetpack);
        this.jetpack = abstractJetpack;
        this.jetpackPlayer = jetpackPlayer;
    }

    @Override
    public void start() {
        long burnRateInSeconds = getAbstractJetpack().getFuelBurnRateInSeconds() * 20;
        setBukkitTask(runTaskTimer(JetpackPlugin.getInstance(), burnRateInSeconds, burnRateInSeconds));
    }

    @Override
    public void run() {
        if (!jetpack.consumeAndCheckFuelStatus()) {
            jetpack.disable(jetpackPlayer.getPlayer());
            stop();
        }
    }

}
