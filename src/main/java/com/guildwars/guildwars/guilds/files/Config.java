package com.guildwars.guildwars.guilds.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Config {

    private static File file;
    private static FileConfiguration configFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/guilds", "config.yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating guilds/config.yml file");
            }
        }
        configFile = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
        configFile.options().copyDefaults(true);
        save();
    }

    public static void loadDefaults() {
        configFile.addDefault("join guild at rank", "RECRUIT");
        configFile.addDefault("max characters in guild name", 15);
        configFile.addDefault("invite expire time (s)", 60);
        configFile.addDefault("truce request expire time (m)", 60);
        configFile.addDefault("max players in guild", 10);
        configFile.addDefault("valid guild name characters", Arrays.asList('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
        'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        configFile.addDefault("blacklisted guild names", Arrays.asList(""));
        configFile.addDefault("power change on death", -1);
        configFile.addDefault("player max power", 5);
        configFile.addDefault("player min power", 0);
        configFile.addDefault("player power regen amount", 1);
        configFile.addDefault("world name", "world");
        configFile.addDefault("max unclaim radius (chunks)", 10);
        configFile.addDefault("max claim radius (chunks)", 3);
        configFile.addDefault("border guild color", "&0");
        configFile.addDefault("raiding duration (min)", 60);
        configFile.addDefault("world claim radius (chunks)", 30);
        configFile.addDefault("g map radius (chunks)", 7);
        configFile.addDefault("online player power regen per min", 0.017);
        configFile.addDefault("offline player power regen per min", 0.0055);
        configFile.addDefault("raiding kick non-raiders update (tick)", 20);
        configFile.addDefault("player chunk update (ticks)", 20);
        configFile.addDefault("own guild name prefix", "&2");
        configFile.addDefault("enemy guild name prefix", "&4");
        configFile.addDefault("other guild name prefix", "&7");
        configFile.addDefault("own guild desc prefix", "&a");
        configFile.addDefault("enemy guild desc prefix", "&c");
        configFile.addDefault("other guild desc prefix", "&f");

        //default permissions
        configFile.createSection("default permissions");
        ConfigurationSection defaultPermissionsSection = configFile.getConfigurationSection("default permissions");
        assert defaultPermissionsSection != null;
        defaultPermissionsSection.addDefault("invite", "MOD");
        defaultPermissionsSection.addDefault("set_desc", "COLEADER");
        defaultPermissionsSection.addDefault("set_name", "COLEADER");
        defaultPermissionsSection.addDefault("chat", "RECRUIT");
        defaultPermissionsSection.addDefault("relations", "MOD");
        defaultPermissionsSection.addDefault("claim", "MOD");
        defaultPermissionsSection.addDefault("unclaim", "MOD");
        defaultPermissionsSection.addDefault("unclaim_all", "COLEADER");
        defaultPermissionsSection.addDefault("sethome", "MOD");

        //default permissions
        configFile.createSection("broadcasts");
        ConfigurationSection broadcastTimings = configFile.getConfigurationSection("broadcasts");
        assert broadcastTimings != null;
        broadcastTimings.addDefault("guilds.fadeIn", 20);
        broadcastTimings.addDefault("guilds.stay", 100);
        broadcastTimings.addDefault("guilds.fadeOut", 40);
        broadcastTimings.addDefault("new territory.fadeIn", 10);
        broadcastTimings.addDefault("new territory.stay", 20);
        broadcastTimings.addDefault("new territory.fadeOut", 10);
    }

    public static FileConfiguration get() {
        return configFile;
    }


    public static void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            System.out.println("Error when saving guilds/config.yml file");
        }
    }

    public static void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}
