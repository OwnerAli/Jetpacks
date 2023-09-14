package me.ogali.jetpacks.items.domain;

import me.ogali.jetpacks.JetpackPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;

public abstract class SwitchableItem {

    private final String id;
    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> inventoryClickEventConsumer;

    public SwitchableItem(String id, ItemStack itemStack, Consumer<InventoryClickEvent> inventoryClickEventConsumer) {
        this.id = id;
        this.itemStack = itemStack;
        this.inventoryClickEventConsumer = inventoryClickEventConsumer;
    }

    public String getId() {
        return id;
    }

    public ItemStack getSwitchableItem() {
        ItemStack clonedItemStack = itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), "switchable-" + id),
                PersistentDataType.STRING, id);
        clonedItemStack.setItemMeta(itemMeta);
        return clonedItemStack;
    }

    public Consumer<InventoryClickEvent> getInventoryClickEventConsumer() {
        return inventoryClickEventConsumer;
    }

    public void register() {
        JetpackPlugin.getInstance().getSwitchableItemRegistry()
                .register(this);
    }

}
