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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.framework.qual.Unused;

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
                .ifPresentOrElse(abstractJetpack -> Chat.tell(player, "&cJetpack with id: " + id + ", already exists!")
                , () -> new JetpackCreationMenu().show(player, new FuelJetpack(id, maxFuelCapacity,
                                fuelBurnPerBurn, fuelBurnRateInSeconds, speed, particle)));
    }

    @Subcommand("get")
    @Syntax("<id>")
    public void onJetpackGet(Player player, String id) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(id)
                .ifPresent(abstractJetpack -> player.getInventory().addItem(abstractJetpack.getJetpackItem()));
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
                                fuelJetpack.getAcceptableFuel().add(itemFuel);
                            }, () -> Chat.tell(player, "&cInvalid fuel id. &7(" + fuelId + ")"));
                }, () -> Chat.tell(player, "&cInvalid jetpack id. &7(" + jetpackId + ")"));
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
    @Syntax("<id>")
    public void onFuelGet(Player player, String id) {
        JetpackPlugin.getInstance().getFuelRegistry()
                .getFuelById(id)
                .filter(abstractFuel -> abstractFuel instanceof ItemFuel)
                .map(abstractFuel -> (ItemFuel) abstractFuel)
                .ifPresent(itemFuel -> player.getInventory().addItem(itemFuel.getItem()));
    }

    @Subcommand("hide-armor-trims")
    public void onHideArmorTrims(Player player) {
        ItemStack itemInPlayerHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInPlayerHand.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        itemInPlayerHand.setItemMeta(itemMeta);
    }

}
