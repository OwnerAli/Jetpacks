package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

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
            Chat.log("key" + key);
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            return getFuelById(key.getKey());
        }
        return Optional.empty();
    }

    public boolean isFuelItem(ItemStack itemStack) {
        return getFuelByItemStack(itemStack).isPresent();
    }

}
