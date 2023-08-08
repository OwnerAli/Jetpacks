package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.events.JetpackArmorUnEquipEvent;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.registries.JetpackRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ApplyItemsToJetpackListener implements Listener {

    private final JetpackPlugin jetpackPlugin;

    public ApplyItemsToJetpackListener(JetpackPlugin jetpackPlugin) {
        this.jetpackPlugin = jetpackPlugin;
    }

    @EventHandler
    public void onItemApply(InventoryClickEvent event) {
        if (event.getSlot() >= 36 && event.getSlot() <= 39) {
            jetpackPlugin.getJetpackRegistry().getJetpackByItemStack(event.getCurrentItem()).flatMap(abstractJetpack ->
                            jetpackPlugin.getJetpackPlayerRegistry().getJetpackPlayerByPlayer((Player) event.getWhoClicked()))
                    .ifPresent(jetpackPlayer -> Bukkit.getPluginManager().callEvent(new JetpackArmorUnEquipEvent(jetpackPlayer)));
            return;
        }

        if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR
                && event.getAction() != InventoryAction.PLACE_ONE
                && event.getAction() != InventoryAction.PLACE_SOME
                && event.getAction() != InventoryAction.PLACE_ALL) return;

        ItemStack cursorItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();

        if (cursorItem == null || currentItem == null) return;
        if (cursorItem.getType() == Material.AIR || currentItem.getType() == Material.AIR) return;

        JetpackRegistry jetpackRegistry = jetpackPlugin.getJetpackRegistry();

        if (!jetpackRegistry.isJetpackItem(currentItem)) return;

        jetpackPlugin.getFuelRegistry().getFuelByItemStack(cursorItem)
                .ifPresent(abstractFuel -> {
                    if (!(abstractFuel instanceof ItemFuel itemFuel)) return;

                    AbstractJetpack abstractJetpack = jetpackRegistry.getJetpackByItemStack(currentItem).get();
                    if (!(abstractJetpack instanceof FuelJetpack fuelJetpack)) return;

                    itemFuel.getClickEventConsumer().accept(event, fuelJetpack);
                });

        jetpackPlugin.getAttachmentRegistry().getAttachmentByItemStack(cursorItem)
                .ifPresent(attachment -> attachment.getClickEventConsumer().accept(event));
    }

}
