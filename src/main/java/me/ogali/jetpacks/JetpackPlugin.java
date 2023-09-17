package me.ogali.jetpacks;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.ogali.jetpacks.attatchments.impl.HarnessAttachment;
import me.ogali.jetpacks.attatchments.impl.TransmitAttachment;
import me.ogali.jetpacks.commands.AdminCommands;
import me.ogali.jetpacks.files.impl.FuelsFile;
import me.ogali.jetpacks.files.impl.JetpacksFile;
import me.ogali.jetpacks.items.impl.FuelExtractorSwitchableItem;
import me.ogali.jetpacks.listeners.*;
import me.ogali.jetpacks.regions.impl.FuelExtractorExecutableRegion;
import me.ogali.jetpacks.regions.impl.JetpackTunerExecutableRegion;
import me.ogali.jetpacks.registries.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class JetpackPlugin extends JavaPlugin {

    public LocationFlag animationFinalLocationFlag = new LocationFlag("animation-final-location");
    public FuelExtractorExecutableRegion fuelExtractorExecutableRegion;
    public JetpackTunerExecutableRegion jetpackTunerExecutableRegion;

    private static JetpackPlugin instance;
    private JetpackPlayerRegistry jetpackPlayerRegistry;
    private JetpackRegistry jetpackRegistry;
    private FuelRegistry fuelRegistry;
    private AttachmentRegistry attachmentRegistry;
    private SwitchableItemRegistry switchableItemRegistry;
    private FuelsFile fuelsFile;
    private JetpacksFile jetpacksFile;

    @Override
    public void onLoad() {
        instance = this;
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        flagRegistry.register(animationFinalLocationFlag);
        this.fuelExtractorExecutableRegion = new FuelExtractorExecutableRegion(flagRegistry);
        this.jetpackTunerExecutableRegion = new JetpackTunerExecutableRegion(flagRegistry);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeRegistries();
        registerListeners();
        registerCommands();
        initializeFiles();
        loadDataFromFiles();
        loadAttachments();
        registerSwitchableItems();
    }

    @Override
    public void onDisable() {
        saveDataToFiles();
    }

    public LocationFlag getAnimationFinalLocationFlag() {
        return animationFinalLocationFlag;
    }

    public FuelExtractorExecutableRegion getFuelExtractorExecutableRegion() {
        return fuelExtractorExecutableRegion;
    }

    public JetpackTunerExecutableRegion getJetpackTunerExecutableRegion() {
        return jetpackTunerExecutableRegion;
    }

    public static JetpackPlugin getInstance() {
        return instance;
    }

    public JetpackPlayerRegistry getJetpackPlayerRegistry() {
        return jetpackPlayerRegistry;
    }

    public JetpackRegistry getJetpackRegistry() {
        return jetpackRegistry;
    }

    public FuelRegistry getFuelRegistry() {
        return fuelRegistry;
    }

    public AttachmentRegistry getAttachmentRegistry() {
        return attachmentRegistry;
    }

    public SwitchableItemRegistry getSwitchableItemRegistry() {
        return switchableItemRegistry;
    }

    private void initializeRegistries() {
        jetpackPlayerRegistry = new JetpackPlayerRegistry();
        jetpackRegistry = new JetpackRegistry();
        fuelRegistry = new FuelRegistry();
        attachmentRegistry = new AttachmentRegistry();
        switchableItemRegistry = new SwitchableItemRegistry();
    }

    private void initializeFiles() {
        this.fuelsFile = new FuelsFile(fuelRegistry);
        this.jetpacksFile = new JetpacksFile(jetpackRegistry);
    }

    private void saveDataToFiles() {
        fuelRegistry.getRegisteredFuels()
                .forEach(abstractFuel -> fuelsFile.saveFuel(abstractFuel));
        jetpackRegistry.getRegisteredJetpacks()
                .forEach(abstractJetpack -> jetpacksFile.saveJetpack(abstractJetpack));
    }

    private void loadDataFromFiles() {
        fuelsFile.loadFuels();
        jetpacksFile.loadJetpacks();
    }

    private void loadAttachments() {
        attachmentRegistry.registerAttachment(new TransmitAttachment());
        attachmentRegistry.registerAttachment(new HarnessAttachment());
    }

    private void registerSwitchableItems() {
        new FuelExtractorSwitchableItem();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerLeaveListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new ApplyItemsToJetpackListener(this), this);
        pluginManager.registerEvents(new PlayerArmorEquipListener(jetpackRegistry), this);
        pluginManager.registerEvents(new PlayerSneakListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new JetpackUnEquipListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(jetpackRegistry, jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerDeathListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerMoveListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new AdminCommands());
        registerCommandCompletions(paperCommandManager);
    }

    private void registerCommandCompletions(PaperCommandManager paperCommandManager) {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = paperCommandManager.getCommandCompletions();

        commandCompletions.registerCompletion("jetpackIdList", handler -> jetpackRegistry.getRegisteredJetpackIds());
        commandCompletions.registerCompletion("fuelIdList", handler -> fuelRegistry.getRegisteredFuelIds());
        commandCompletions.registerCompletion("attachmentIdList", handler -> attachmentRegistry.getRegisteredAttachmentIds());
        commandCompletions.registerCompletion("switchableItemIdList", handler -> switchableItemRegistry.getRegisteredItemIds());
    }

}
