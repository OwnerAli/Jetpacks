package me.ogali.jetpacks.fuels.domain;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractFuel {

    private final String id;
    private final ItemStack fuelItem;

    protected AbstractFuel(String id, ItemStack fuelItem) {
        this.id = id;
        this.fuelItem = fuelItem;
    }

}
