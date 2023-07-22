package me.ogali.jetpacks.jetpacks.domain;

import me.ogali.jetpacks.JetpackPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

public abstract class AbstractJetpack {

    private final String id;

    private ItemStack jetpackItem;

    private int maxFuelCapacity;
    private int fuelBurnAmountPerBurnRate;
    private long fuelBurnRateInSeconds;
    private double speed;
    private Particle smokeParticle;

    private int currentFuelAmount;
    private boolean enabled;

    protected AbstractJetpack(String id, int maxFuelCapacity, int fuelBurnAmountPerBurnRate, long fuelBurnRateInSeconds, double speed, Particle smokeParticle) {
        this.id = id;
        this.maxFuelCapacity = maxFuelCapacity;
        this.fuelBurnAmountPerBurnRate = fuelBurnAmountPerBurnRate;
        this.fuelBurnRateInSeconds = fuelBurnRateInSeconds;
        this.speed = speed;
        this.smokeParticle = smokeParticle;
    }

    public String getId() {
        return id;
    }

    public ItemStack getJetpackItem() {
        ItemMeta itemMeta = jetpackItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), id), PersistentDataType.STRING, id);
        jetpackItem.setItemMeta(itemMeta);
        return jetpackItem;
    }

    public void setJetpackItem(ItemStack jetpackItem) {
        this.jetpackItem = jetpackItem;
    }

    public int getMaxFuelCapacity() {
        return maxFuelCapacity;
    }

    public void setMaxFuelCapacity(int maxFuelCapacity) {
        this.maxFuelCapacity = maxFuelCapacity;
    }

    public int getFuelBurnAmountPerBurnRate() {
        return fuelBurnAmountPerBurnRate;
    }

    public void setFuelBurnAmountPerBurnRate(int fuelBurnAmountPerBurnRate) {
        this.fuelBurnAmountPerBurnRate = fuelBurnAmountPerBurnRate;
    }

    public int getCurrentFuelAmount() {
        return currentFuelAmount;
    }

    public void setCurrentFuelAmount(int currentFuelAmount) {
        this.currentFuelAmount = currentFuelAmount;
        updateFuelAmountInItemLore();
    }

    public long getFuelBurnRateInSeconds() {
        return fuelBurnRateInSeconds;
    }

    public void setFuelBurnRateInSeconds(long fuelBurnRateInSeconds) {
        this.fuelBurnRateInSeconds = fuelBurnRateInSeconds;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public Particle getSmokeParticle() {
        return smokeParticle;
    }

    public void setSmokeParticle(Particle smokeParticle) {
        this.smokeParticle = smokeParticle;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void toggle(Player player);

    private void updateFuelAmountInItemLore() {
        List<Component> jetpackItemLoreList = jetpackItem.getItemMeta().lore();

        if (jetpackItemLoreList == null) return;
        jetpackItemLoreList.stream()
                .filter(jetpackItemLore -> jetpackItemLore.contains(Component.text("Fuel Level: ")))
                .findFirst()
                .ifPresent(component -> component.replaceText(builder -> builder.replacement("Fuel Level: " + getCurrentFuelAmount())));
    }

}
