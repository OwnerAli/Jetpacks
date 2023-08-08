package me.ogali.jetpacks.files.impl;

import me.ogali.jetpacks.files.domain.JsonFile;
import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.registries.FuelRegistry;
import me.ogali.jetpacks.utils.Serialization;
import org.bukkit.inventory.ItemStack;

public class FuelsFile extends JsonFile {

    private final FuelRegistry fuelRegistry;

    public FuelsFile(FuelRegistry fuelRegistry) {
        super("fuels");
        this.fuelRegistry = fuelRegistry;
    }

    public void saveFuel(AbstractFuel abstractFuel) {
        if (abstractFuel instanceof ItemFuel itemFuel) {
            saveItemFuel(itemFuel);
        }
    }

    public void loadFuels() {
        singleLayerKeySet()
                .forEach(fuelId -> {
                    if (getString(fuelId + ".type").equalsIgnoreCase("item")) {
                        ItemStack fuelItem = Serialization.deserialize(getString(fuelId + ".itemstack"));

                        ItemFuel abstractFuel = new ItemFuel(fuelId);
                        abstractFuel.setItem(fuelItem);
                        fuelRegistry.registerFuel(abstractFuel);
                    }
                });
    }

    private void saveItemFuel(ItemFuel itemFuel) {
        String jetpackId = itemFuel.getId();

        set(jetpackId + ".itemstack", Serialization.serialize(itemFuel.getItem()));
        set(jetpackId + ".type", "item");
    }


}
