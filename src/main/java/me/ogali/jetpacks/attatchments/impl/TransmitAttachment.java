package me.ogali.jetpacks.attatchments.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.attatchments.domain.Attachment;
import me.ogali.jetpacks.attatchments.domain.AttachmentAction;
import me.ogali.jetpacks.attatchments.domain.impl.ClickShiftAttachmentAction;
import me.ogali.jetpacks.cooldowns.Cooldown;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TransmitAttachment implements Attachment {

    private boolean sentNotEnoughFuelMessage;
    private long lastCooldownTime;

    @Override
    public String getId() {
        return "transmit";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FIREWORK_STAR)
                .setName("&d(-) Transmit Attachment")
                .addLoreLines("&7Allows you to transmit", "&7your physical form 2 blocks", "&7ahead of your current location.", "", "Trigger Type: Click-Crouch")
                .setPersistentData(new NamespacedKey(JetpackPlugin.getInstance(), "attachment-" + getId()), getId())
                .build();
    }

    @Override
    public String getDisplayName() {
        return Chat.colorizeHex("&5&lTransmit Attachment");
    }

    @Override
    public ClickShiftAttachmentAction getTriggerAction() {
        return jetpackPlayer -> {
            Player player = jetpackPlayer.getPlayer();
            AbstractJetpack currentJetpack = jetpackPlayer.getCurrentJetpack();
            Cooldown attachmentCooldown = jetpackPlayer.getAttachmentCooldown();

            if (attachmentCooldown.isActive()) {
                if (attachmentCooldown.getRemainingTimeInSeconds() % 2 != 0) return;
                if (lastCooldownTime == attachmentCooldown.getRemainingTimeInSeconds()) return;
                lastCooldownTime = attachmentCooldown.getRemainingTimeInSeconds();
                Chat.tellFormatted(player, "&cYou're still on cooldown for another %s second(s)!", attachmentCooldown
                        .getRemainingTimeInSeconds());
                return;
            }
            if (currentJetpack instanceof FuelJetpack fuelJetpack) {
                if (!fuelJetpack.consumeAndCheckFuelStatus(5)) {
                    if (sentNotEnoughFuelMessage) return;
                    sentNotEnoughFuelMessage = true;
                    Chat.tell(player, "&cYou don't have enough fuel to transmit!");
                    return;
                }
                fuelJetpack.consumeAndCheckFuelStatus(5);
                Chat.tell(player, "&c&l-5 FUEL");
                attachmentCooldown.start();
                lastCooldownTime = 0;
            }

            player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 10, 10);
            player.spawnParticle(Particle.PORTAL, player.getLocation(), 20);
            Location playerLocation = player.getLocation();
            Vector playerDirection = playerLocation.getDirection().normalize();

            Location newLocation = playerLocation.clone().add(playerDirection.multiply(2));

            Vector velocity = newLocation.toVector().subtract(playerLocation.toVector());
            player.setVelocity(velocity);
        };
    }

    @Override
    public AttachmentAction getCancellationAction() {
        return null;
    }

}
