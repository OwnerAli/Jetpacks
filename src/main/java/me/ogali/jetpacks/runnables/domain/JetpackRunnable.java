package me.ogali.jetpacks.runnables.domain;

import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class JetpackRunnable extends BukkitRunnable {

    private final AbstractJetpack abstractJetpack;
    private BukkitTask bukkitTask;

    protected JetpackRunnable(AbstractJetpack abstractJetpack) {
        this.abstractJetpack = abstractJetpack;
    }

    public AbstractJetpack getAbstractJetpack() {
        return abstractJetpack;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    public void setBukkitTask(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    public abstract void start();

    public abstract void stop();

}
