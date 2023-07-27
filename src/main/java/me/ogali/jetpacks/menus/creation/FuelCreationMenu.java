package me.ogali.jetpacks.menus.creation;

import com.github.stefvanschie.inventoryframework.gui.type.DispenserGui;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FuelCreationMenu {

    private ItemStack lastItem;

    public void show(Player player, ItemFuel itemFuel) {
        DispenserGui gui = new DispenserGui(Chat.colorize("&8&lInsert Fuel Item Below"));
        gui.setOnGlobalClick(click -> {
            click.setCancelled(true);

            if (click.getClickedInventory() == click.getView().getTopInventory()) {
                ItemStack item = click.getInventory().getItem(4);
                if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE && item.getType() != Material.AIR) {
                    itemFuel.setItem(item);
                    JetpackPlugin.getInstance().getFuelRegistry().registerFuel(itemFuel);
                    player.closeInventory();
                    return;
                }
            }

            if (click.getClickedInventory() != click.getView().getBottomInventory()) return;
            if (click.getCurrentItem() == lastItem) return;
            if (click.getCurrentItem() == null) return;

            lastItem = click.getCurrentItem();
            for (int i = 0; i < gui.getInventory().getSize(); i++) {
                if (i == 4) continue;
                gui.getInventory().setItem(i, new ItemBuilder(Material.LIME_DYE).setName("&a&lCLICK TO CONFIRM!").build());
            }
            click.getInventory().setItem(4, click.getCurrentItem());
        });

        gui.show(player);
    }

}
