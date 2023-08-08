package me.ogali.jetpacks.utils;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.attatchments.domain.Attachment;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistentDataUtils {

    private static final NamespacedKey JETPACK_FUEL_LEVEL_KEY = new NamespacedKey(JetpackPlugin.getInstance(), "fuelLevel");

    public static void setFuelOfItem(ItemStack jetpackItem, double fuel) {
        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(JETPACK_FUEL_LEVEL_KEY, PersistentDataType.DOUBLE, fuel);

        jetpackItem.setItemMeta(itemMeta);
    }

    public static void addFuelToItem(ItemStack jetpackItem, double fuelAmountToAdd) {
        double fuelLevelFromItem = getFuelLevelFromItem(jetpackItem);
        double newFuelLevel = fuelLevelFromItem + fuelAmountToAdd;

        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(JETPACK_FUEL_LEVEL_KEY, PersistentDataType.DOUBLE, newFuelLevel);

        jetpackItem.setItemMeta(itemMeta);
    }

    public static Double getFuelLevelFromItem(ItemStack jetpackItem) {
        if (jetpackItem == null || !jetpackItem.hasItemMeta()) return 0.0;
        PersistentDataContainer persistentDataContainer = jetpackItem.getItemMeta().getPersistentDataContainer();
        Double fuelLevelFromJetpack = persistentDataContainer.get(JETPACK_FUEL_LEVEL_KEY, PersistentDataType.DOUBLE);
        return fuelLevelFromJetpack == null ? 0.0 : fuelLevelFromJetpack;
    }

    public static void addAttachment(ItemStack itemStack, String attachmentId) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        Optional<Attachment> attachment = JetpackPlugin.getInstance().getAttachmentRegistry()
                .getAttachmentById(attachmentId);
        if (attachment.isEmpty()) return;

        persistentDataContainer.set(new NamespacedKey(JetpackPlugin.getInstance(), "attachment-" + attachment.get().getId()),
                PersistentDataType.STRING, attachment.get().getId());
        itemStack.setItemMeta(itemMeta);
        Chat.log("KEYS: " + itemMeta.getPersistentDataContainer().getKeys());
        itemMeta.getPersistentDataContainer().getKeys()
                .forEach(namespacedKey -> Chat.log(namespacedKey.getKey()));
    }

    public static List<Attachment> getCurrentAttachments(PersistentDataContainer persistentDataContainer) {
        if (persistentDataContainer == null) return null;
        return persistentDataContainer.getKeys().stream()
                .filter(namespacedKey -> namespacedKey.getKey().contains("attachment-"))
                .map(namespacedKey -> namespacedKey.getKey().split("-")[1])
                .map(key -> JetpackPlugin.getInstance().getAttachmentRegistry().getAttachmentById(key)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}