package me.ogali.jetpacks.regions.domain;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.ogali.jetpacks.JetpackPlugin;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ExecutableRegion {

    protected LocationFlag animationFinalLocationFlag;
    private final List<Flag<?>> regionFlags;

    public ExecutableRegion() {
        this.regionFlags = new ArrayList<>();
        this.animationFinalLocationFlag = JetpackPlugin.getInstance().getAnimationFinalLocationFlag();
    }

    public abstract void execute(ProtectedRegion protectedRegion, Event event);

    protected void addFlags(Flag<?>... flags) {
        regionFlags.addAll(List.of(flags));
    }

    protected void registerFlags(FlagRegistry flagRegistry) {
        regionFlags.forEach(flagRegistry::register);
    }

    protected ArmorStand getAnimationArmorstand(Player player, ItemStack itemStack) {
        return (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.COMMAND,
                entity -> {
                    ArmorStand armorstand = (ArmorStand) entity;

                    armorstand.setVisible(false);
                    armorstand.setItem(EquipmentSlot.CHEST, itemStack);
                    armorstand.setGravity(false);
                    armorstand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
                    armorstand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
                });
    }

}
