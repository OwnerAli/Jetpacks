package me.ogali.jetpacks.menus.creation;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.menus.items.navigation.impl.ConfirmButton;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JetpackCreationMenu {

    public void show(Player player, AbstractJetpack abstractJetpack) {
        ChestGui gui = new ChestGui(4, Chat.colorize("&8&lInsert Jetpack Item Below"));
        StaticPane staticPane = new StaticPane(0, 0, 9, 4);
        gui.setOnGlobalClick(click -> {
            click.setCancelled(true);
            if (click.getClickedInventory() == click.getView().getBottomInventory()) {
                click.getInventory().setItem(13, click.getCurrentItem());
            }
        });

        staticPane.fillWith(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName("#2CA8C2&lINSERT JETPACK ITEM BELOW (ARMOR)")
                .build()), 4, 0);
        gui.getInventory().setItem(13, new ItemStack(Material.AIR));
        staticPane.addItem(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName("#2CA8C2&lINSERT JETPACK ITEM ABOVE (ARMOR)")
                .build()), 4, 2);

        staticPane.addItem(new ConfirmButton(click -> {
            ItemStack itemInMiddleSlot = gui.getInventory().getItem(13);

            if (itemInMiddleSlot == null) return;
            if (itemInMiddleSlot.getType() == Material.AIR) return;

            abstractJetpack.setJetpackItem(itemInMiddleSlot);
            JetpackPlugin.getInstance().getJetpackRegistry().registerJetpack(abstractJetpack);
        }), 4, 3);

        gui.addPane(staticPane);
        gui.show(player);
    }

}
