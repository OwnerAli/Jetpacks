package me.ogali.jetpacks.files.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.files.domain.JsonFile;
import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.Serialization;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class JetpacksFile extends JsonFile {

    private final JetpackRegistry jetpackRegistry;

    public JetpacksFile(JetpackRegistry jetpackRegistry) {
        super("jetpacks");
        this.jetpackRegistry = jetpackRegistry;
    }

    public void saveJetpack(AbstractJetpack abstractJetpack) {
        if (abstractJetpack instanceof FuelJetpack fuelJetpack) {
            saveFuelJetpack(fuelJetpack);
        }
    }

    public void loadJetpacks() {
        singleLayerKeySet()
                .forEach(jetpackId -> {
                    if (getString(jetpackId + ".type").equalsIgnoreCase("fuel")) {
                        ItemStack jetpackItem = Serialization.deserialize(getString(jetpackId + ".itemstack"));
                        int maxFuelCapacity = getInt(jetpackId + ".maxFuelCapacity");
                        double fuelBurnAmountPerBurn = getDouble(jetpackId + ".fuelBurnAmountPerBurn");
                        long fuelBurnRateInSeconds = getLong(jetpackId + ".fuelBurnRateInSeconds");
                        double speed = getDouble(jetpackId + ".speed");
                        Particle particle = Particle.CAMPFIRE_COSY_SMOKE;

                        try {
                            particle = Particle.valueOf(getString(jetpackId + ".particle"));
                        } catch (IllegalArgumentException ignored) {
                            Chat.log("&cInvalid particle name defined. Setting to default smoke particle.");
                        }

                        FuelJetpack abstractJetpack = new FuelJetpack(jetpackId, maxFuelCapacity, fuelBurnAmountPerBurn,
                                fuelBurnRateInSeconds, speed, particle);
                        abstractJetpack.setJetpackItem(jetpackItem);
                        getStringList(jetpackId + ".acceptableFuel").forEach(fuelId -> JetpackPlugin.getInstance().getFuelRegistry()
                                .getFuelById(fuelId)
                                .ifPresent(abstractJetpack::addAcceptableFuel));
                        jetpackRegistry.registerJetpack(abstractJetpack);
                    }
                });
    }

    private void saveFuelJetpack(FuelJetpack fuelJetpack) {
        String jetpackId = fuelJetpack.getId();

        set(jetpackId + ".itemstack", Serialization.serialize(fuelJetpack.getJetpackItem()));
        set(jetpackId + ".maxFuelCapacity", fuelJetpack.getMaxFuelCapacity());
        set(jetpackId + ".fuelBurnAmountPerBurn", fuelJetpack.getFuelBurnAmountPerBurnRate());
        set(jetpackId + ".fuelBurnRateInSeconds", fuelJetpack.getFuelBurnRateInSeconds());
        set(jetpackId + ".speed", fuelJetpack.getSpeed());
        set(jetpackId + ".particle", fuelJetpack.getSmokeParticle().name());
        set(jetpackId + ".acceptableFuel", fuelJetpack.getAcceptableFuelSet().stream().map(AbstractFuel::getId).toList());
        set(jetpackId + ".type", "fuel");
    }

}