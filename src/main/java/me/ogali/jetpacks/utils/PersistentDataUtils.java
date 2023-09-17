package me.ogali.jetpacks.utils;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.attatchments.domain.Attachment;
import me.ogali.jetpacks.fuels.domain.FuelHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class PersistentDataUtils {

    public static void setFuelOfItem(ItemStack jetpackItem, FuelHolder fuelHolder) {
        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        ArrayList<NamespacedKey> fuelNamespacedKeys = new ArrayList<>();

        for (NamespacedKey key : persistentDataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("fuellevel-")) continue;
            fuelNamespacedKeys.add(key);
        }

        Map<String, Double> fuelTypeAmountMap = fuelHolder.getFuelTypeAmountMap();

        fuelNamespacedKeys.forEach(namespacedKey -> {
            String fuelId = namespacedKey.getKey().split("-")[1];
            Double fuelAmount = fuelTypeAmountMap.getOrDefault(fuelId, 0.0);

            if (fuelAmount == 0.0) {
                persistentDataContainer.remove(namespacedKey);
                return;
            }
            persistentDataContainer.set(namespacedKey, PersistentDataType.DOUBLE, fuelAmount);
        });

        jetpackItem.setItemMeta(itemMeta);
    }

    public static void setFuelOfItem(ItemStack jetpackItem, double amount) {
        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        ArrayList<NamespacedKey> fuelNamespacedKeys = new ArrayList<>();

        for (NamespacedKey key : persistentDataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("fuellevel-")) continue;
            fuelNamespacedKeys.add(key);
        }

        fuelNamespacedKeys.forEach(namespacedKey -> persistentDataContainer.set(namespacedKey, PersistentDataType.DOUBLE, amount));

        jetpackItem.setItemMeta(itemMeta);
    }

    public static void addFuelToItem(ItemStack jetpackItem, double fuelAmountToAdd, String fuelId) {
        Optional<FuelHolder> fuelHolderFromItem = getFuelHolderFromItem(jetpackItem);

        if (fuelHolderFromItem.isEmpty()) return;
        FuelHolder fuelHolder = fuelHolderFromItem.get();

        Double fuelLevelFromItem = fuelHolder.getFuelTypeAmountMap().getOrDefault(fuelId, 0.0);

        double newFuelLevel = fuelLevelFromItem + fuelAmountToAdd;

        ItemMeta itemMeta = jetpackItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey(JetpackPlugin.getInstance(), "fuelLevel-" + fuelId),
                PersistentDataType.DOUBLE, newFuelLevel);
        fuelHolder.incrementFuelAmount(fuelId, fuelAmountToAdd);

        jetpackItem.setItemMeta(itemMeta);
    }

    public static Optional<FuelHolder> getFuelHolderFromItem(ItemStack jetpackItem) {
        if (jetpackItem == null || !jetpackItem.hasItemMeta()) return Optional.empty();
        PersistentDataContainer persistentDataContainer = jetpackItem.getItemMeta().getPersistentDataContainer();

        ArrayList<NamespacedKey> fuelNamespacedKeys = new ArrayList<>();
        Map<String, Double> fuelHolderMap = new HashMap<>();

        for (NamespacedKey key : jetpackItem.getItemMeta().getPersistentDataContainer().getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("fuellevel-")) continue;
            fuelNamespacedKeys.add(key);
        }

        fuelNamespacedKeys.forEach(namespacedKey -> {
            Double fuelLevelFromJetpack = persistentDataContainer.get(namespacedKey, PersistentDataType.DOUBLE);
            fuelHolderMap.put(namespacedKey.getKey().split("-")[1], fuelLevelFromJetpack);
        });

        return Optional.of(new FuelHolder(fuelHolderMap));
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