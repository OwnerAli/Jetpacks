package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.domain.AbstractFuel;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.registries.FuelRegistry;
import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FuelApplyListener implements Listener {

    private final JetpackPlugin jetpackPlugin;

    public FuelApplyListener(JetpackPlugin jetpackPlugin) {
        this.jetpackPlugin = jetpackPlugin;
    }

    @EventHandler
    public void onFuelApply(InventoryClickEvent event) {
        if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR
                && event.getAction() != InventoryAction.PLACE_ONE
                && event.getAction() != InventoryAction.PLACE_SOME
                && event.getAction() != InventoryAction.PLACE_ALL) return;

        ItemStack cursorItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();

        if (cursorItem == null || currentItem == null) return;

        JetpackRegistry jetpackRegistry = jetpackPlugin.getJetpackRegistry();
        FuelRegistry fuelRegistry = jetpackPlugin.getFuelRegistry();

        if (!fuelRegistry.isFuelItem(cursorItem) || !jetpackRegistry.isJetpackItem(currentItem)) return;

        AbstractJetpack abstractJetpack = jetpackRegistry.getJetpackByItemStack(currentItem).orElse(null);
        AbstractFuel abstractFuel = fuelRegistry.getFuelByItemStack(cursorItem).orElse(null);

        if (!(abstractJetpack instanceof FuelJetpack fuelJetpack) || !(abstractFuel instanceof ItemFuel itemFuel))
            return;

        if (!fuelJetpack.getAcceptableFuel().contains(itemFuel)) {
            Chat.tell(event.getWhoClicked(), "&cThis jetpack doesn't accept this type of fuel!");
            return;
        }

        PersistentDataUtils.updateItemFuelLevel(currentItem, cursorItem.getAmount());
        Chat.tell(event.getWhoClicked(), "&e&lJETPACK &f→ &eAdded &f" + cursorItem.getAmount() + " &efuel to your jetpack!");
        cursorItem.setAmount(0);
    }

}
