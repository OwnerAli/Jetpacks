package me.ogali.jetpacks.jetpacks.impl;

import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.impl.FuelBurnRunnable;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class FuelJetpack extends AbstractJetpack {

    private final Set<AbstractFuel> acceptableFuel;

    public FuelJetpack(String id, int maxFuelCapacity, int fuelBurnAmountPerBurnRate, long fuelBurnRateInSeconds, double speed, Particle particle) {
        super(id, maxFuelCapacity, fuelBurnAmountPerBurnRate, fuelBurnRateInSeconds, speed, particle);
        this.acceptableFuel = new HashSet<>();
    }

    public Set<AbstractFuel> getAcceptableFuel() {
        return acceptableFuel;
    }

    @Override
    public void toggle(Player player) {
        if (isEnabled()) {
            setEnabled(false);
            player.setAllowFlight(false);
            player.setFlying(false);
            return;
        }
        setEnabled(true);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
        new FuelBurnRunnable(this).start();
    }

}
