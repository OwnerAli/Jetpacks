package me.ogali.jetpacks.regions.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.domain.FuelHolder;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.regions.domain.ExecutableRegion;
import me.ogali.jetpacks.runnables.impl.JetpackTuningAnimationRunnable;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class JetpackTunerExecutableRegion extends ExecutableRegion {

    private final BooleanFlag jetpackTuningFlag;

    public JetpackTunerExecutableRegion(FlagRegistry flagRegistry) {
        this.jetpackTuningFlag = new BooleanFlag("jetpack-tuning-region");
        addFlags(jetpackTuningFlag);
        registerFlags(flagRegistry);
    }

    public BooleanFlag getJetpackTuningFlag() {
        return jetpackTuningFlag;
    }

    @Override
    public void execute(ProtectedRegion protectedRegion, Event event) {
        if (protectedRegion.getFlag(animationFinalLocationFlag) == null) {
            Chat.log("&cYou made an error while setting up the jetpack tuning region.");
            return;
        }
        if (!(event instanceof PlayerDropItemEvent playerDropItemEvent)) return;

        ItemStack itemStack = playerDropItemEvent.getItemDrop().getItemStack();
        Optional<AbstractJetpack> jetpackByItemStack = JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackByItemStack(itemStack);

        if (jetpackByItemStack.isEmpty()) return;

        playerDropItemEvent.setCancelled(true);

        if (PersistentDataUtils.getFuelHolderFromItem(itemStack).isEmpty()) return;

        FuelHolder fuelHolder = PersistentDataUtils.getFuelHolderFromItem(itemStack).get();

        Player player = playerDropItemEvent.getPlayer();
        if (!fuelHolder.hasNoFuel()) {
            Chat.tell(player, "&cYou need to drain your jetpack of fuel before tuning.");
            return;
        }

        ArmorStand animationArmorstand = getAnimationArmorstand(player, itemStack);
        itemStack.setAmount(0);

        new JetpackTuningAnimationRunnable(player, animationArmorstand, jetpackByItemStack.get(), fuelHolder,
                BukkitAdapter.adapt(protectedRegion.getFlag(animationFinalLocationFlag)), 1).start();
    }

}
