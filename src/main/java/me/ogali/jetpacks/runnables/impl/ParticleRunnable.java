package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.domain.JetpackRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleRunnable extends JetpackRunnable {

    private final AbstractJetpack jetpack;
    private final Player player;

    public ParticleRunnable(AbstractJetpack abstractJetpack, Player player) {
        super(abstractJetpack);
        this.jetpack = abstractJetpack;
        this.player = player;
    }

    @Override
    public void start() {
        setBukkitTask(runTaskTimer(JetpackPlugin.getInstance(), 0, 10));
    }

    @Override
    public void run() {
        if (!jetpack.isEnabled()) {
            stop();
            return;
        }
        Location jetpackLocation = player.getLocation().clone().add(0, 1, 0);
        jetpackLocation.getWorld().spawnParticle(jetpack.getSmokeParticle(), jetpackLocation, 5);
    }

}
