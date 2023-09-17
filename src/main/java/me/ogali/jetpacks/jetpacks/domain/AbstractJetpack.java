package me.ogali.jetpacks.jetpacks.domain;

import me.ogali.jetpacks.JetpackPlugin;
import me.ogali.jetpacks.attatchments.domain.Attachment;
import me.ogali.jetpacks.attatchments.domain.impl.ClickShiftAttachmentAction;
import me.ogali.jetpacks.attatchments.domain.impl.HoldShiftAttachmentAction;
import me.ogali.jetpacks.fuels.domain.FuelHolder;
import me.ogali.jetpacks.players.JetpackPlayer;
import me.ogali.jetpacks.runnables.impl.FuelBurnRunnable;
import me.ogali.jetpacks.runnables.impl.ParticleRunnable;
import me.ogali.jetpacks.utils.Chat;
import me.ogali.jetpacks.utils.PersistentDataUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJetpack {

    private final String id;

    private ItemStack jetpackItem;
    private int maxFuelCapacity;
    private double fuelBurnAmountPerBurnRate;
    private long fuelBurnRateInSeconds;
    private double speed;
    private Particle smokeParticle;
    private final List<Attachment> attachmentList;

    private FuelHolder fuelHolder;
    private boolean enabled;

    protected AbstractJetpack(AbstractJetpack original) {
        this.id = original.id;
        this.jetpackItem = original.jetpackItem;
        this.maxFuelCapacity = original.maxFuelCapacity;
        this.fuelBurnAmountPerBurnRate = original.fuelBurnAmountPerBurnRate;
        this.fuelBurnRateInSeconds = original.fuelBurnRateInSeconds;
        this.speed = original.speed;
        this.smokeParticle = original.smokeParticle;
        this.fuelHolder = new FuelHolder(original.fuelHolder);
        this.attachmentList = original.attachmentList;
        this.enabled = original.enabled;
    }

    protected AbstractJetpack(String id, int maxFuelCapacity, double fuelBurnAmountPerBurnRate, long fuelBurnRateInSeconds, double speed, Particle smokeParticle) {
        this.id = id;
        this.maxFuelCapacity = maxFuelCapacity;
        this.fuelBurnAmountPerBurnRate = fuelBurnAmountPerBurnRate;
        this.fuelBurnRateInSeconds = fuelBurnRateInSeconds;
        this.speed = speed;
        this.smokeParticle = smokeParticle;
        this.attachmentList = new ArrayList<>();
        this.fuelHolder = new FuelHolder();
    }

    public String getId() {
        return id;
    }

    public ItemStack getJetpackItem() {
        ItemStack clonedJetpackItem = jetpackItem.clone();
        ItemMeta itemMeta = clonedJetpackItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JetpackPlugin.getInstance(), "jetpack-" + id),
                PersistentDataType.STRING, id);
        clonedJetpackItem.setItemMeta(itemMeta);
        return clonedJetpackItem;
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

    public double getFuelBurnAmountPerBurnRate() {
        return fuelBurnAmountPerBurnRate;
    }

    public void setFuelBurnAmountPerBurnRate(double fuelBurnAmountPerBurnRate) {
        this.fuelBurnAmountPerBurnRate = fuelBurnAmountPerBurnRate;
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

    public FuelHolder getFuelHolder() {
        return fuelHolder;
    }

    public boolean consumeAndCheckFuelStatus() {
        return fuelHolder.consumeAndCheckFuelStatus(fuelBurnAmountPerBurnRate);
    }

    public boolean consumeAndCheckFuelStatus(int amountToConsume) {
        return fuelHolder.consumeAndCheckFuelStatus(amountToConsume);
    }

    public void addAttachment(Attachment attachment) {
        attachmentList.add(attachment);
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void triggerClickShiftAttachments(JetpackPlayer jetpackPlayer) {
        attachmentList.forEach(attachment -> {
            if (!(attachment.getTriggerAction() instanceof ClickShiftAttachmentAction attachmentAction)) return;
            attachmentAction.trigger(jetpackPlayer);
        });
    }

    public void triggerHoldShiftAttachments(JetpackPlayer jetpackPlayer) {
        attachmentList.forEach(attachment -> {
            if (!(attachment.getTriggerAction() instanceof HoldShiftAttachmentAction attachmentAction)) return;
            attachmentAction.trigger(jetpackPlayer);
        });
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void toggle(JetpackPlayer jetpackPlayer);

    public void enable(JetpackPlayer jetpackPlayer) {
        Player player = jetpackPlayer.getPlayer();

        if (fuelHolder.hasNoFuel()) {
            Chat.tell(player, "&e&lJETPACK &f→ &c&lOut of fuel!");
            return;
        }
        setEnabled(true);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed((float) speed / 10);
        new FuelBurnRunnable(this, jetpackPlayer).start();
        new ParticleRunnable(this, player).start();
        Chat.tell(player, "&e&lJETPACK &f→ &e&lENABLED");
    }

    public void disable(Player player) {
        if (!enabled) return;
        setEnabled(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(1);
        Chat.tell(player, "&e&lJETPACK &f→ &c&lDISABLED");
    }

    public void disable(Player player, ItemStack jetpackItem) {
        PersistentDataUtils.setFuelOfItem(jetpackItem, fuelHolder);
        disable(player);
    }

    public void setFuelHolder(FuelHolder fuelHolder) {
        this.fuelHolder = fuelHolder;
    }
}
