package me.ogali.jetpacks.utils;

import me.ogali.jetpacks.JetpackPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PersistentDataUtils {

    private static final NamespacedKey JETPACK_FUEL_LEVEL_KEY = new NamespacedKey(JetpackPlugin.getInstance(), "fuelLevel");

    public static void updateItemFuelLevel(ItemStack jetpackItem, double fuelAmountToAdd) {
        double fuelLevelFromItem = getFuelLevelFromItem(jetpackItem);

        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(JETPACK_FUEL_LEVEL_KEY, PersistentDataType.DOUBLE, fuelLevelFromItem + fuelAmountToAdd);

        jetpackItem.setItemMeta(itemMeta);
    }

    public static Double getFuelLevelFromItem(ItemStack jetpackItem) {
        if (jetpackItem == null || !jetpackItem.hasItemMeta()) return 0.0;
        PersistentDataContainer dataContainer = jetpackItem.getItemMeta().getPersistentDataContainer();
        Double fuelLevelFromJetpack = dataContainer.get(JETPACK_FUEL_LEVEL_KEY, PersistentDataType.DOUBLE);
        return fuelLevelFromJetpack == null ? 0.0 : fuelLevelFromJetpack;
    }

}