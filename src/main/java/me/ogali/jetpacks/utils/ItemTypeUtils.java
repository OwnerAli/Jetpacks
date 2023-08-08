package me.ogali.jetpacks.utils;

import org.bukkit.Material;

public class ItemTypeUtils {

    public static boolean isArmor(Material material) {
        return material == Material.LEATHER_HELMET ||
                material == Material.LEATHER_CHESTPLATE ||
                material == Material.LEATHER_LEGGINGS ||
                material == Material.LEATHER_BOOTS ||
                material == Material.CHAINMAIL_HELMET ||
                material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.CHAINMAIL_LEGGINGS ||
                material == Material.CHAINMAIL_BOOTS ||
                material == Material.IRON_HELMET ||
                material == Material.IRON_CHESTPLATE ||
                material == Material.IRON_LEGGINGS ||
                material == Material.IRON_BOOTS ||
                material == Material.GOLDEN_HELMET ||
                material == Material.GOLDEN_CHESTPLATE ||
                material == Material.GOLDEN_LEGGINGS ||
                material == Material.GOLDEN_BOOTS ||
                material == Material.DIAMOND_HELMET ||
                material == Material.DIAMOND_CHESTPLATE ||
                material == Material.DIAMOND_LEGGINGS ||
                material == Material.DIAMOND_BOOTS ||
                material == Material.TURTLE_HELMET;
    }

}
