package me.ogali.jetpacks.menus.items;

import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.Material;

public class PromptItem extends ItemBuilder {

    public PromptItem(String text) {
        super(Material.PAPER);
        setName(text);
    }

}
