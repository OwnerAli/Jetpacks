package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FuelRegistry {

    private final Map<String, AbstractFuel> fuelMap = new HashMap<>();

    public void registerFuel(AbstractFuel abstractFuel) {
        fuelMap.put(abstractFuel.getId(), abstractFuel);
    }

    public void unregisterFuelById(String id) {
        fuelMap.remove(id);
    }

    public Optional<AbstractFuel> getFuelById(String id) {
        return Optional.ofNullable(fuelMap.get(id));
    }

    public Optional<AbstractFuel> getFuelByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return Optional.empty();
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        for (NamespacedKey key : dataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("fuel-")) continue;
            return getFuelById(key.getKey().split("-")[1]);
        }
        return Optional.empty();
    }

    public boolean isFuelItem(ItemStack itemStack) {
        return getFuelByItemStack(itemStack).isPresent();
    }

    public Collection<AbstractFuel> getRegisteredFuels() {
        return fuelMap.values();
    }

    public Collection<String> getRegisteredFuelIds() {
        return fuelMap.keySet();
    }

}
