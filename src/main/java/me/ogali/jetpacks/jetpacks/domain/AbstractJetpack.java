package me.ogali.jetpacks.jetpacks.domain;

import me.ogali.jetpacks.JetpackPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public abstract class AbstractJetpack {

    private final String id;

    private ItemStack jetpackItem;

    private int maxFuelCapacity;
    private int fuelBurnAmountPerBurnRate;
    private long fuelBurnRateInSeconds;
    private double speed;
    private Particle smokeParticle;

    private double currentFuelLevel;
    private boolean enabled;

    protected AbstractJetpack(AbstractJetpack original) {
        this.id = original.id;
        this.jetpackItem = copyItemStack(original.jetpackItem);
        this.maxFuelCapacity = original.maxFuelCapacity;
        this.fuelBurnAmountPerBurnRate = original.fuelBurnAmountPerBurnRate;
        this.fuelBurnRateInSeconds = original.fuelBurnRateInSeconds;
        this.speed = original.speed;
        this.smokeParticle = original.smokeParticle;
        this.currentFuelLevel = original.currentFuelLevel;
        this.enabled = original.enabled;
    }

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

    public double getCurrentFuelLevel() {
        return currentFuelLevel;
    }

    public void setCurrentFuelLevel(double currentFuelLevel) {
        this.currentFuelLevel = currentFuelLevel;
        updateFuelAmountInItemLore();
    }

    public void addToCurrentFuelLevel(int fuelAmountToAdd) {
        this.currentFuelLevel += fuelAmountToAdd;
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
                .ifPresent(component -> component.replaceText(builder -> builder.replacement("Fuel Level: " + getCurrentFuelLevel())));
    }

    private ItemStack copyItemStack(ItemStack original) {
        if (original == null) return null;

        ItemStack copy = original.clone();
        ItemMeta originalMeta = original.getItemMeta();
        if (originalMeta != null) {
            ItemMeta copyMeta = copy.getItemMeta();
            copyMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), id), PersistentDataType.STRING, id);
            copy.setItemMeta(copyMeta);
        }
        return copy;
    }

}
