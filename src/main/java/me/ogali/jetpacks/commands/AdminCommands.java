package me.ogali.jetpacks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.jetpacks.impl.FuelJetpack;
import me.ogali.jetpacks.menus.creation.JetpackCreationMenu;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

@CommandAlias("jp|jetpack|jetpacks")
@CommandPermission("jetpacks.admin")
public class AdminCommands extends BaseCommand {

    @Subcommand("create")
    @Syntax("<id> <max fuel capacity> <fuel burn amount per burn> <fuel burn rate in seconds> <speed> <smoke particle>")
    @CommandCompletion("@particleList")
    public void onCreate(Player player, String id, int maxFuelCapacity, int fuelBurnPerBurn, long fuelBurnRateInSeconds, double speed, Particle particle) {
        new JetpackCreationMenu().show(player, new FuelJetpack(id, maxFuelCapacity,
                fuelBurnPerBurn, fuelBurnRateInSeconds, speed, particle));
    }

    @Subcommand("get")
    @Syntax("<id>")
    public void onGet(Player player, String id) {
        JetpackPlugin.getInstance().getJetpackRegistry()
                .getJetpackById(id)
                .ifPresent(abstractJetpack -> player.getInventory().addItem(abstractJetpack.getJetpackItem()));
    }

}
