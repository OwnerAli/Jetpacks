package me.ogali.jetpacks.runnables.impl;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.fuels.domain.FuelHolder;
import me.ogali.jetpacks.fuels.impl.ItemFuel;
import me.ogali.jetpacks.jetpacks.domain.AbstractJetpack;
import me.ogali.jetpacks.runnables.animations.ArmorStandMover;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class FuelExtractorAnimationRunnable extends ArmorStandMover {

    private final Player player;
    private final ArmorStand armorStand;
    private final AbstractJetpack abstractJetpack;
    private final FuelHolder fuelHolder;
    private final Location targetLocation;

    public FuelExtractorAnimationRunnable(Player player, ArmorStand armorStand, AbstractJetpack abstractJetpack, FuelHolder fuelHolder,
                                          Location targetLocation, int durationSeconds) {
        super(armorStand, targetLocation, durationSeconds);
        this.player = player;
        this.armorStand = armorStand;
        this.abstractJetpack = abstractJetpack;
        this.fuelHolder = fuelHolder;
        this.targetLocation = targetLocation;
    }

    @Override
    public void run() {
        super.run();
        if (armorStand.getLocation().distance(targetLocation) <= 0.1) {
            this.cancel();
            runFuelDropAnimation();
        }
    }

    private void runFuelDropAnimation() {
        fuelHolder.getFuelTypeAmountMap().forEach((fuelId, fuelAmount) -> JetpackPlugin.getInstance().getFuelRegistry()
                .getFuelById(fuelId)
                .ifPresent(abstractFuel -> {
                    if (!(abstractFuel instanceof ItemFuel itemFuel)) return;

                    ItemStack fuelItemClone = itemFuel.getItem().clone();

                    // List of items dropped in animation
                    List<Item> itemList = new ArrayList<>();

                    // Rounded remaining fuel amount because it is a double
                    int roundedFuelAmount = (int) Math.round(fuelAmount);

                    for (int i = 0; i < roundedFuelAmount; i++) {
                        player.getWorld().dropItemNaturally(armorStand.getLocation(), fuelItemClone, floorItem -> {
                            floorItem.setCustomNameVisible(true);
                            floorItem.setCanPlayerPickup(false);
                            floorItem.setGlowing(true);
                            itemList.add(floorItem);
                        });
                    }

                    // Set fuel item clone to be the amount of rounded fuel left so player gets back right amnt of fuel
                    fuelItemClone.setAmount(roundedFuelAmount);

                    // Remove dropped animation items after 3 seconds
                    Bukkit.getScheduler().runTaskLater(JetpackPlugin.getInstance(), () -> {
                        armorStand.remove();
                        itemList.forEach(Item::remove);
                    }, 20 * 3);

                    // Give player back jetpack and fuel left in jetpack
                    giveExtractedFuelToPlayer(fuelItemClone);
                }));
        giveJetpackToPlayer();
    }

    private void giveExtractedFuelToPlayer(ItemStack fuelItemClone) {
        PlayerInventory inventory = player.getInventory();

        if (inventory.firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), fuelItemClone);
            return;
        }
        inventory.addItem(fuelItemClone);
    }

    private void giveJetpackToPlayer() {
        PlayerInventory inventory = player.getInventory();

        if (inventory.firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), abstractJetpack.getJetpackItem());
            return;
        }
        inventory.addItem(abstractJetpack.getJetpackItem());
    }

}
