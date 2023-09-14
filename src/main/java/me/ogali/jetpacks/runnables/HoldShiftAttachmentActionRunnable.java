package me.ogali.jetpacks.runnables;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.players.JetpackPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HoldShiftAttachmentActionRunnable extends BukkitRunnable {

    private final JetpackPlayer jetpackPlayer;
    private BukkitTask bukkitTask;

    public HoldShiftAttachmentActionRunnable(JetpackPlayer jetpackPlayer) {
        this.jetpackPlayer = jetpackPlayer;
    }

    public void start() {
        this.bukkitTask = runTaskTimerAsynchronously(JetpackPlugin.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        if (!jetpackPlayer.getPlayer().isSneaking()) {
            bukkitTask.cancel();
            return;
        }
        jetpackPlayer.getCurrentJetpack().triggerHoldShiftAttachments(jetpackPlayer);
    }

}
