package me.ogali.jetpacks.players;

import me.ogali.jetpacks.attatchments.domain.Attachment;
import me.ogali.jetpacks.cooldowns.Cooldown;
import me.ogali.jetpacks.fuels.domain.FuelHolder;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

public class JetpackPlayer {

    private final Player player;
    private AbstractJetpack currentJetpack;
    private ItemStack jetpackItem;
    private final Cooldown attachmentCooldown;

    public JetpackPlayer(Player player) {
        this.player = player;
        this.attachmentCooldown = new Cooldown(5);
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractJetpack getCurrentJetpack() {
        return currentJetpack;
    }

    public ItemStack getJetpackItem() {
        return jetpackItem;
    }

    public void setCurrentJetpack(AbstractJetpack abstractJetpack, ItemStack jetpackItem) {
        if (!(abstractJetpack instanceof FuelJetpack fuelJetpack)) return;

        unEquipCurrentJetpack();
        currentJetpack = new FuelJetpack(fuelJetpack);
        Optional<FuelHolder> fuelHolderFromItem = PersistentDataUtils.getFuelHolderFromItem(jetpackItem);
        currentJetpack.setFuelHolder(fuelHolderFromItem.orElseGet(FuelHolder::new));

        ItemMeta itemMeta = jetpackItem.getItemMeta();
        List<Attachment> itemAttachmentsList = PersistentDataUtils.getCurrentAttachments(itemMeta.getPersistentDataContainer());

        if (itemAttachmentsList.isEmpty()) return;
        itemAttachmentsList.forEach(attachment -> currentJetpack.addAttachment(attachment));
    }

    public void setJetpackItem(ItemStack jetpackItem) {
        this.jetpackItem = jetpackItem;
    }

    public Cooldown getAttachmentCooldown() {
        return attachmentCooldown;
    }

    public void unEquipCurrentJetpack() {
        if (currentJetpack == null) return;
        currentJetpack.disable(player, jetpackItem);
        this.currentJetpack = null;
        this.jetpackItem = null;
    }

}
