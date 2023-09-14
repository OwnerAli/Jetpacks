package me.ogali.jetpacks.fuels.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.BiConsumer;

public class ItemFuel extends AbstractFuel {

    private final BiConsumer<InventoryClickEvent, FuelJetpack> clickEventConsumer;
    private ItemStack item;

    public ItemFuel(String id) {
        super(id);
        this.clickEventConsumer = (click, fuelJetpack) -> {
            ItemStack jetpackItem = click.getCurrentItem();
            ItemStack fuelItem = click.getCursor();

            if (!fuelJetpack.getAcceptableFuelSet().contains(this)) {
                Chat.tellFormatted(click.getWhoClicked(), "&cJetpack %s does not support this fuel!", fuelJetpack.getId());
                return;
            }
            click.setCancelled(true);
            PersistentDataUtils.addFuelToItem(jetpackItem, fuelItem.getAmount(), getId());
            Chat.tell(click.getWhoClicked(), "&e&lJETPACK &f→ &eAdded &f" + fuelItem.getAmount() + " &efuel to your jetpack!");
            fuelItem.setAmount(0);
        };
    }

    public BiConsumer<InventoryClickEvent, FuelJetpack> getClickEventConsumer() {
        return clickEventConsumer;
    }

    public ItemStack getItem() {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), "fuel-" + getId()),
                PersistentDataType.STRING, getId());
        item.setItemMeta(itemMeta);
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

}
