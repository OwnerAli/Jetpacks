package me.ogali.jetpacks.attatchments.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.attatchments.domain.Attachment;
import me.ogali.jetpacks.attatchments.domain.impl.HoldShiftAttachmentAction;
import me.ogali.jetpacks.attatchments.domain.impl.MoveAttachmentAction;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class HarnessAttachment implements Attachment {

    private BukkitTask fuelGenerateTask;
    private boolean sentNotEnoughFuelMessage;
    private long lastCooldownTime;

    @Override
    public String getId() {
        return "harness";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FIREWORK_STAR)
                .setName("&2&lHarness Attachment")
                .addLoreLines("&7Allows you to harness", "&7the natural planetary energy", "to fuel your jetpack.", "", "Trigger Type: Hold-Crouch")
                .setPersistentData(new NamespacedKey(JetpackPlugin.getInstance(), "attachment-" + getId()), getId())
                .build();
    }

    @Override
    public String getDisplayName() {
        return "&2(-) Harness Attachment";
    }

    @Override
    public HoldShiftAttachmentAction getTriggerAction() {
        return jetpackPlayer -> {
            Player player = jetpackPlayer.getPlayer();

            if (!player.isSneaking()) {
                fuelGenerateTask.cancel();
                return;
            }
            fuelGenerateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(JetpackPlugin.getInstance(),
                    () -> {
                        player.spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0, 1, 0), 10,
                                new Particle.DustOptions(Color.GREEN, 1));
                        jetpackPlayer.getCurrentJetpack().setCurrentFuelLevel(jetpackPlayer.getCurrentJetpack()
                                .getCurrentFuelLevel() + 5);
                        Chat.tell(player, "&2&l+5 FUEL");
                    }, 20 * 5, 20 * 5);
        };
    }

    @Override
    public MoveAttachmentAction getCancellationAction() {
        return jetpackPlayer -> {
            Player player = jetpackPlayer.getPlayer();
            player.setSneaking(false);
            Chat.tell(player, "&cYou moved so your gather attachment has stopped and is now on cooldown!");
        };
    }

}
