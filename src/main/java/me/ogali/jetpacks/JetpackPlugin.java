package me.ogali.jetpacks;

import co.aikar.commands.*;
import me.ogali.jetpacks.commands.AdminCommands;
import me.ogali.jetpacks.listeners.FuelApplyListener;
import me.ogali.jetpacks.listeners.PlayerArmorEquipListener;
import me.ogali.jetpacks.listeners.PlayerSneakListener;
import me.ogali.jetpacks.listeners.PlayerToggleFlyListener;
import me.ogali.jetpacks.registries.JetpackPlayerRegistry;
import me.ogali.jetpacks.registries.JetpackRegistry;
import me.ogali.jetpacks.utils.Chat;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JetpackPlugin extends JavaPlugin {

    private static JetpackPlugin instance;
    private JetpackRegistry jetpackRegistry;
    private JetpackPlayerRegistry jetpackPlayerRegistry;

    @Override
    public void onEnable() {
        instance = this;
        initializeRegistries();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
    }

    private void initializeRegistries() {
        jetpackRegistry = new JetpackRegistry();
        jetpackPlayerRegistry = new JetpackPlayerRegistry();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerToggleFlyListener(), this);
        pluginManager.registerEvents(new FuelApplyListener(), this);
        pluginManager.registerEvents(new PlayerArmorEquipListener(), this);
        pluginManager.registerEvents(new PlayerSneakListener(jetpackRegistry), this);
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

        // Add the REDSTONE particle values
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

    public static JetpackPlugin getInstance() {
        return instance;
    }

    public JetpackRegistry getJetpackRegistry() {
        return jetpackRegistry;
    }

    public JetpackPlayerRegistry getJetpackPlayerRegistry() {
        return jetpackPlayerRegistry;
    }

}
