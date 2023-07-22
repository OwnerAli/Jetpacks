package me.ogali.jetpacks.listeners;

import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class PlayerSneakListener implements Listener {

    private final JetpackRegistry jetpackRegistry;

    public PlayerSneakListener(JetpackRegistry jetpackRegistry) {
        this.jetpackRegistry = jetpackRegistry;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        for (ItemStack itemStack : event.getPlayer().getInventory().getArmorContents()) {
            if (itemStack == null || !itemStack.hasItemMeta()) continue;
            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();

            for (NamespacedKey key : dataContainer.getKeys()) {
                if (!key.getNamespace().equalsIgnoreCase("jetpacks") ||
                        !jetpackRegistry.isJetpackId(key.getKey())) continue;
                jetpackRegistry.getJetpackById(key.getKey()).ifPresent(abstractJetpack -> abstractJetpack.toggle(event.getPlayer()));
                break;
            }
        }
    }

}
