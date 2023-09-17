package me.ogali.jetpacks.runnables.animations;

import me.ogali.jetpacks.JetpackPlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class ArmorStandMover extends BukkitRunnable {

    private final ArmorStand armorStand;
    private final Location startLocation;
    private final Location targetLocation;
    private final int durationTicks;
    private int currentTick = 0;

    public ArmorStandMover(ArmorStand armorStand, Location targetLocation, int durationSeconds) {
        this.armorStand = armorStand;
        this.startLocation = armorStand.getLocation();
        this.targetLocation = targetLocation;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks
    }

    @Override
    public void run() {
        Location newLocation = getNewArmorstandLocation();
        armorStand.teleport(newLocation);
        currentTick++;
    }

    public void start() {
        runTaskTimer(JetpackPlugin.getInstance(), 0L, 1L);
    }

    @NotNull
    private Location getNewArmorstandLocation() {
        double progress = (double) currentTick / durationTicks;
        double deltaX = targetLocation.getX() - startLocation.getX();
        double deltaY = targetLocation.getY() - startLocation.getY();
        double deltaZ = targetLocation.getZ() - startLocation.getZ();

        double newX = startLocation.getX() + progress * deltaX;
        double newY = startLocation.getY() + progress * deltaY;
        double newZ = startLocation.getZ() + progress * deltaZ;

        return new Location(armorStand.getWorld(), newX, newY, newZ);
    }

}
