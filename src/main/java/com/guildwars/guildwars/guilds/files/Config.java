package com.guildwars.guildwars.guilds.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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

        //default permissions
        configFile.createSection("default permissions");
        ConfigurationSection defaultPermissionsSection = configFile.getConfigurationSection("default permissions");
        assert defaultPermissionsSection != null;
        defaultPermissionsSection.addDefault("invite", "MOD");
        defaultPermissionsSection.addDefault("set_desc", "COLEADER");
        defaultPermissionsSection.addDefault("set_name", "COLEADER");
        defaultPermissionsSection.addDefault("chat", "RECRUIT");
        defaultPermissionsSection.addDefault("relations", "COLEADER");
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
