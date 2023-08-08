package me.ogali.jetpacks.registries;

import me.ogali.jetpacks.attatchments.domain.Attachment;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AttachmentRegistry {

    private final Map<String, Attachment> attachmentMap = new HashMap<>();

    public void registerAttachment(Attachment attachment) {
        attachmentMap.put(attachment.getId(), attachment);
    }

    public void unRegisterAttachment(String attachmentId) {
        attachmentMap.remove(attachmentId);
    }

    public Optional<Attachment> getAttachmentById(String attachmentId) {
        return Optional.ofNullable(attachmentMap.get(attachmentId));
    }

    public Optional<Attachment> getAttachmentByItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return Optional.empty();
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        for (NamespacedKey key : dataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("jetpacks")) continue;
            if (!key.getKey().contains("attachment-") || key.getKey().contains("fuel-") || key.getKey().contains("jetpack-")) continue;
            return getAttachmentById(key.getKey().split("-")[1]);
        }
        return Optional.empty();
    }

    public boolean isAttachmentItem(ItemStack itemStack) {
        return getAttachmentByItemStack(itemStack).isPresent();
    }

    public Collection<String> getRegisteredAttachmentIds() {
        return attachmentMap.keySet();
    }

}
