package me.ogali.jetpacks.items;

import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.Material;

public class FuelExtractorItem extends ItemBuilder {

    public FuelExtractorItem() {
        super(Material.HOPPER_MINECART);
        setName("&a&lFuel Extractor");
        addLoreLines("&fDrag and Drop this item", "&fonto your jetpack to drain", "&fand collect it's fuel.");
        glowing();
    }

}
