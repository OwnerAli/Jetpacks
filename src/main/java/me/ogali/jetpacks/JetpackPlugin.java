package me.ogali.jetpacks;

import co.aikar.commands.*;
import me.ogali.jetpacks.attatchments.impl.HarnessAttachment;
import me.ogali.jetpacks.attatchments.impl.TransmitAttachment;
import me.ogali.jetpacks.commands.AdminCommands;
import me.ogali.jetpacks.files.impl.FuelsFile;
import me.ogali.jetpacks.files.impl.JetpacksFile;
import me.ogali.jetpacks.listeners.*;
import me.ogali.jetpacks.registries.AttachmentRegistry;
import me.ogali.jetpacks.registries.FuelRegistry;
import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import me.ogali.jetpacks.registries.JetpackRegistry;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JetpackPlugin extends JavaPlugin {

    private static JetpackPlugin instance;
    private JetpackPlayerRegistry jetpackPlayerRegistry;
    private JetpackRegistry jetpackRegistry;
    private FuelRegistry fuelRegistry;
    private AttachmentRegistry attachmentRegistry;
    private FuelsFile fuelsFile;
    private JetpacksFile jetpacksFile;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initializeRegistries();
        registerListeners();
        registerCommands();
        initializeFiles();
        loadDataFromFiles();
        loadAttachments();
    }

    @Override
    public void onDisable() {
        saveDataToFiles();
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

    private void initializeRegistries() {
        jetpackPlayerRegistry = new JetpackPlayerRegistry();
        jetpackRegistry = new JetpackRegistry();
        fuelRegistry = new FuelRegistry();
        attachmentRegistry = new AttachmentRegistry();
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

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ApplyItemsToJetpackListener(this), this);
        pluginManager.registerEvents(new PlayerArmorEquipListener(jetpackRegistry), this);
        pluginManager.registerEvents(new PlayerSneakListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerJoinListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new JetpackUnEquipListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(jetpackRegistry, jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerDeathListener(jetpackPlayerRegistry), this);
        pluginManager.registerEvents(new PlayerMoveListener(jetpackPlayerRegistry), this);
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new AdminCommands());
        registerCommandContexts(paperCommandManager);
        registerCommandCompletions(paperCommandManager);
    }

    private void registerCommandCompletions(PaperCommandManager paperCommandManager) {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = paperCommandManager.getCommandCompletions();
        List<String> allParticleNames = Arrays.stream(Particle.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Add colored REDSTONE particle values
        allParticleNames.add("REDSTONE_RED");
        allParticleNames.add("REDSTONE_GREEN");
        allParticleNames.add("REDSTONE_BLUE");
        allParticleNames.add("REDSTONE_WHITE");
        allParticleNames.add("REDSTONE_YELLOW");
        allParticleNames.add("REDSTONE_PURPLE");
        allParticleNames.add("REDSTONE_ORANGE");
        allParticleNames.add("REDSTONE_GRAY");
        allParticleNames.add("REDSTONE_BLACK");

        commandCompletions.registerCompletion("particleList", handler -> allParticleNames);
        commandCompletions.registerCompletion("jetpackIdList", handler -> getJetpackRegistry().getRegisteredJetpackIds());
        commandCompletions.registerCompletion("fuelIdList", handler -> getFuelRegistry().getRegisteredFuelIds());
        commandCompletions.registerCompletion("attachmentIdList", handler -> getAttachmentRegistry().getRegisteredAttachmentIds());
    }

    private void registerCommandContexts(PaperCommandManager paperCommandManager) {
        CommandContexts<BukkitCommandExecutionContext> commandContexts = paperCommandManager.getCommandContexts();
        commandContexts.registerContext(Particle.class, context -> {
            String particleEnumName = context.popFirstArg();

            if (particleEnumName.isEmpty() || particleEnumName.isBlank()) {
                return Particle.CAMPFIRE_COSY_SMOKE;
            }

            return switch (particleEnumName) {
                case "REDSTONE_RED" -> Particle.REDSTONE.builder().color(Color.RED).particle();
                case "REDSTONE_GREEN" -> Particle.REDSTONE.builder().color(Color.GREEN).particle();
                case "REDSTONE_BLUE" -> Particle.REDSTONE.builder().color(Color.BLUE).particle();
                case "REDSTONE_WHITE" -> Particle.REDSTONE.builder().color(Color.WHITE).particle();
                case "REDSTONE_YELLOW" -> Particle.REDSTONE.builder().color(Color.YELLOW).particle();
                case "REDSTONE_PURPLE" -> Particle.REDSTONE.builder().color(Color.PURPLE).particle();
                case "REDSTONE_ORANGE" -> Particle.REDSTONE.builder().color(Color.ORANGE).particle();
                case "REDSTONE_GRAY" -> Particle.REDSTONE.builder().color(Color.GRAY).particle();
                case "REDSTONE_BLACK" -> Particle.REDSTONE.builder().color(Color.BLACK).particle();
                default -> {
                    try {
                        yield Particle.valueOf(particleEnumName);
                    } catch (IllegalArgumentException ex) {
                        throw new InvalidCommandArgument("Invalid particle value: " + particleEnumName);
                    }
                }
            };
        });
    }

}
