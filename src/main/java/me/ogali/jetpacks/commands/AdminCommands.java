package me.ogali.jetpacks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.menus.creation.FuelCreationMenu;
import me.ogali.jetpacks.menus.creation.JetpackCreationMenu;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("unused")
@CommandAlias("jp|jetpack|jetpacks")
@CommandPermission("jetpacks.admin")
public class AdminCommands extends BaseCommand {

    @Subcommand("create")
    @Syntax("<id> <max fuel capacity> <fuel burn amount per burn> <fuel burn rate in seconds> <speed> <smoke particle>")
    @CommandCompletion("@particleList")
    public void onJetpackCreate(Player player, String id, int maxFuelCapacity, int fuelBurnPerBurn, long fuelBurnRateInSeconds, double speed, Particle particle) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(id)
                .ifPresentOrElse(abstractJetpack -> Chat.tell(player, "&cJetpack with id: " + id + ", already exists!"),
                        () -> new JetpackCreationMenu().show(player, new FuelJetpack(id, maxFuelCapacity,
                                fuelBurnPerBurn, fuelBurnRateInSeconds, speed, particle)));
    }

    @Subcommand("get")
    @Syntax("<id>")
    @CommandCompletion("@jetpackIdList")
    public void onJetpackGet(Player player, String id) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(id)
                .ifPresentOrElse(abstractJetpack -> {
                            player.getInventory().addItem(abstractJetpack.getJetpackItem());
                            Chat.tellFormatted(player, "&aAdded %s jetpack to your inventory!", id);
                        },
                        () -> Chat.tellFormatted(player, "&cInvalid jetpack id: %s", id));
    }

    @Subcommand("give")
    @Syntax("<id>")
    @CommandCompletion("@jetpackIdList")
    public void onJetpackGive(CommandSender commandSender, Player receiver, String id) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(id)
                .ifPresentOrElse(abstractJetpack -> {
                            receiver.getInventory().addItem(abstractJetpack.getJetpackItem());
                            Chat.tellFormatted(commandSender, "&aAdded %s jetpack to %s's inventory!", id, receiver.getName());
                            Chat.tellFormatted(receiver, "&aAdded %s jetpack to your inventory!", id);
                        },
                        () -> Chat.tellFormatted(commandSender, "&cInvalid jetpack id: %s", id));
    }

    @Subcommand("add-acceptable-fuel")
    @Syntax("<jetpack id> <fuel id>")
    public void onAddAcceptableFuel(Player player, String jetpackId, String fuelId) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(jetpackId)
                .ifPresentOrElse(abstractJetpack -> {
                    if (!(abstractJetpack instanceof FuelJetpack fuelJetpack)) return;
                    JetpackPlugin.getInstance().getFuelRegistry().getFuelById(fuelId)
                            .ifPresentOrElse(abstractFuel -> {
                                if (!(abstractFuel instanceof ItemFuel itemFuel)) return;
                                fuelJetpack.getAcceptableFuelSet().add(itemFuel);
                                Chat.tellFormatted(player, "Added %s as an acceptable fuel for %s!", fuelId, jetpackId);
                            }, () -> Chat.tell(player, "&cInvalid fuel id. &7(" + fuelId + ")"));
                }, () -> Chat.tell(player, "&cInvalid jetpack id. &7(" + jetpackId + ")"));
    }

    @Subcommand("attachment get")
    @CommandCompletion("@attachmentIdList")
    public void onGetAttachmentItem(Player player, String attachmentId) {
        JetpackPlugin.getInstance().getAttachmentRegistry().getAttachmentById(attachmentId).ifPresent(attachment ->
                player.getInventory().addItem(attachment.getItem()));
    }

    @Subcommand("fuel create")
    @Syntax("<id>")
    public void onFuelCreate(Player player, String id) {
        JetpackPlugin.getInstance().getFuelRegistry()
                .getFuelById(id)
                .ifPresentOrElse(abstractJetpack -> Chat.tell(player, "&cFuel with id: " + id + ", already exists!")
                        , () -> new FuelCreationMenu().show(player, new ItemFuel(id)));
    }

    @Subcommand("fuel get")
    @Syntax("<id> (<amount>)")
    @CommandCompletion("@fuelIdList")
    public void onFuelGet(Player player, String id, @Optional Integer amount) {
        JetpackPlugin.getInstance().getFuelRegistry()
                .getFuelById(id)
                .filter(abstractFuel -> abstractFuel instanceof ItemFuel)
                .map(abstractFuel -> (ItemFuel) abstractFuel)
                .ifPresent(itemFuel -> {
                    if (amount == null) {
                        player.getInventory().addItem(itemFuel.getItem());
                    } else {
                        ItemStack item = itemFuel.getItem().clone();
                        item.setAmount(amount);
                        player.getInventory().addItem(item);
                    }
                    Chat.tellFormatted(player, "&aAdded %sx %s fuel to your inventory!", amount == null ? 1 : amount, id);
                });
    }

    @Subcommand("item-utils hideArmorTrims")
    public void onHideArmorTrims(Player player) {
        ItemStack itemInPlayerHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInPlayerHand.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        itemInPlayerHand.setItemMeta(itemMeta);
    }

}
