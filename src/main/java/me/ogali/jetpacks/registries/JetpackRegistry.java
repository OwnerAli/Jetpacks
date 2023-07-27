package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JetpackRegistry {

    private final Map<String, AbstractJetpack> abstractJetpackMap = new HashMap<>();

    public void registerJetpack(AbstractJetpack abstractJetpack) {
        abstractJetpackMap.put(abstractJetpack.getId(), abstractJetpack);
    }

    public void unregisterJetpackById(String id) {
        abstractJetpackMap.remove(id);
    }

    public Optional<AbstractJetpack> getJetpackById(String id) {
        return Optional.ofNullable(abstractJetpackMap.get(id));
    }

    public Optional<AbstractJetpack> getJetpackByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return Optional.empty();
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        for (NamespacedKey key : dataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            return getJetpackById(key.getKey());
        }
        return Optional.empty();
    }

    public boolean isJetpackId(String id) {
        return abstractJetpackMap.containsKey(id);
    }

    public boolean isJetpackItem(ItemStack itemStack) {
        return getJetpackByItemStack(itemStack).isPresent();
    }

}
