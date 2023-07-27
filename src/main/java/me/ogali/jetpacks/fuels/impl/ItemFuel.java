package me.ogali.jetpacks.fuels.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemFuel extends AbstractFuel {

    private ItemStack item;

    public ItemFuel(String id) {
        super(id);
    }

    public ItemStack getItem() {
        ItemMeta itemMeta = item.getItemMeta();
        Chat.log(getId());
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), getId()), PersistentDataType.STRING, getId());
        item.setItemMeta(itemMeta);
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

}
