package me.ogali.jetpacks.jetpacks.impl;

import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.players.JetpackPlayer;
import me.ogali.jetpacks.runnables.impl.FuelBurnRunnable;
import me.ogali.jetpacks.runnables.impl.ParticleRunnable;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FuelJetpack extends AbstractJetpack {

    private final Set<AbstractFuel> acceptableFuelSet;

    public FuelJetpack(String id, int maxFuelCapacity, double fuelBurnAmountPerBurnRate, long fuelBurnRateInSeconds, double speed, Particle particle) {
        super(id, maxFuelCapacity, fuelBurnAmountPerBurnRate, fuelBurnRateInSeconds, speed, particle);
        this.acceptableFuelSet = new HashSet<>();
    }

    public FuelJetpack(FuelJetpack original) {
        super(original);
        this.acceptableFuelSet = new HashSet<>(original.acceptableFuelSet);
    }

    public Set<AbstractFuel> getAcceptableFuelSet() {
        return acceptableFuelSet;
    }

    public void addAcceptableFuel(AbstractFuel abstractFuel) {
        acceptableFuelSet.add(abstractFuel);
    }

    @Override
    public void toggle(JetpackPlayer jetpackPlayer) {
        if (isEnabled()) {
            disable(jetpackPlayer.getPlayer());
            return;
        }
        enable(jetpackPlayer);
    }

}
