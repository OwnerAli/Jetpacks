package me.ogali.jetpacks.menus.items.navigation.impl;

import me.ogali.jetpacks.menus.items.navigation.domain.NavigationButton;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ConfirmButton extends NavigationButton {

    public ConfirmButton(Consumer<InventoryClickEvent> clickEventConsumer) {
        super("CONFIRM", clickEventConsumer);
    }

}