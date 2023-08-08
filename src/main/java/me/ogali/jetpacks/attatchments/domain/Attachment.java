package me.ogali.jetpacks.attatchments.domain;

import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.ItemBuilder;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface Attachment {

    String getId();
    ItemStack getItem();
    String getDisplayName();
    AttachmentAction getTriggerAction();
    AttachmentAction getCancellationAction();

    default Consumer<InventoryClickEvent> getClickEventConsumer() {
        return click -> {
            ItemStack jetpackItem = click.getCurrentItem();
            ItemStack attachmentItem = click.getCursor();

            click.setCancelled(true);
            PersistentDataUtils.addAttachment(jetpackItem, getId());
            click.getWhoClicked().getInventory().setItem(click.getSlot(), new ItemBuilder(jetpackItem).buildWithAttachmentLore(this));
            attachmentItem.setAmount(attachmentItem.getAmount() - 1);
            Chat.tell(click.getWhoClicked(), "&aAttachment added! &7(" + getDisplayName() + ")");
        };
    }

}
