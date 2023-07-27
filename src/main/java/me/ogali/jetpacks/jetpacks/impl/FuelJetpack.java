package me.ogali.jetpacks.jetpacks.impl;

import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.impl.FuelBurnRunnable;
import me.ogali.jetpacks.runnables.impl.ParticleRunnable;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FuelJetpack extends AbstractJetpack {

    private final Set<AbstractFuel> acceptableFuel;

    public FuelJetpack(String id, int maxFuelCapacity, int fuelBurnAmountPerBurnRate, long fuelBurnRateInSeconds, double speed, Particle particle) {
        super(id, maxFuelCapacity, fuelBurnAmountPerBurnRate, fuelBurnRateInSeconds, speed, particle);
        this.acceptableFuel = new HashSet<>();
    }

    public FuelJetpack(FuelJetpack original) {
        super(original);
        this.acceptableFuel = new HashSet<>(original.acceptableFuel);
    }

    public Set<AbstractFuel> getAcceptableFuel() {
        return acceptableFuel;
    }

    @Override
    public void toggle(Player player) {
        if (isEnabled()) {
            disable(player);
            return;
        }
        enable(player);
    }

    private void enable(Player player) {
        if (getCurrentFuelLevel() <= 0 || getCurrentFuelLevel() - getFuelBurnAmountPerBurnRate() <= 0) {
            Chat.tell(player, "&c&ljetpack out of fuel!");
            return;
        }
        setEnabled(true);
        player.setAllowFlight(true);
        player.setFlying(true);
        new FuelBurnRunnable(this, player).start();
        new ParticleRunnable(this, player).start();
        Chat.tell(player, "&e&lJETPACK &f→ &e&lENABLED");
    }

    private void disable(Player player) {
        setEnabled(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        Chat.tell(player, "&e&lJETPACK &f→ &c&lDISABLED");
    }

}
