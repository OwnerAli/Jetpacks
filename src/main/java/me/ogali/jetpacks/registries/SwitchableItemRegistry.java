package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.items.domain.SwitchableItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SwitchableItemRegistry {

    private final Map<String, SwitchableItem> switchableItemMap = new HashMap<>();

    public void register(SwitchableItem switchableItem) {
        switchableItemMap.put(switchableItem.getId().toLowerCase(), switchableItem);
    }

    public void unRegister(String id) {
        switchableItemMap.remove(id);
    }

    public Optional<SwitchableItem> getSwitchableItemById(String id) {
        return Optional.ofNullable(switchableItemMap.get(id));
    }

    public Optional<SwitchableItem> getSwitchableItemByItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return Optional.empty();
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        for (NamespacedKey key : dataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("switchable-")) continue;
            return getSwitchableItemById(key.getKey().split("-")[1]);
        }
        return Optional.empty();
    }

    public Collection<String> getRegisteredItemIds() {
        return switchableItemMap.keySet();
    }

}
