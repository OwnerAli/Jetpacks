package me.ogali.jetpacks.menus.items.navigation.domain;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public abstract class NavigationButton extends GuiItem {

    public NavigationButton(String displayName, Consumer<InventoryClickEvent> clickEventConsumer) {
        super(new ItemBuilder(Material.ARROW)
                .setName("&a&l" + displayName)
                .build(), clickEventConsumer);
    }

}
