package me.ogali.jetpacks.items.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.items.FuelExtractorItem;
import me.ogali.jetpacks.items.domain.SwitchableItem;
import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FuelExtractorSwitchableItem extends SwitchableItem {

    public FuelExtractorSwitchableItem() {
        super("fuelExtractor", new FuelExtractorItem().build(), inventoryClickEvent -> {
            ItemStack cursorItem = inventoryClickEvent.getCursor();
            ItemStack currentItem = inventoryClickEvent.getCurrentItem();

            if (currentItem == null) return;
            if (currentItem.getType() == Material.AIR) return;

            JetpackRegistry jetpackRegistry = JetpackPlugin.getInstance().getJetpackRegistry();

            if (!jetpackRegistry.isJetpackItem(currentItem)) return;
            inventoryClickEvent.setCancelled(true);
            cursorItem.setAmount(currentItem.getAmount() - 1);

            jetpackRegistry.getJetpackByItemStack(currentItem)
                    .ifPresent(abstractJetpack -> {
                        abstractJetpack.getFuelHolder().getFuelTypeAmountMap()
                                .forEach((fuelId, fuelAmount) -> JetpackPlugin.getInstance().getFuelRegistry().getFuelById(fuelId)
                                        .ifPresent(abstractFuel -> {
                                            if (!(abstractFuel instanceof ItemFuel itemFuel)) return;
                                            ItemStack clonedFuelItem = itemFuel.getItem().clone();
                                            clonedFuelItem.setAmount((int) Math.round(fuelAmount));

                                            inventoryClickEvent.getWhoClicked().getInventory().addItem(clonedFuelItem);
                                        }));
                        PersistentDataUtils.setFuelOfItem(currentItem, 0);
                    });
        });
        register();
    }

}
