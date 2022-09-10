package com.guildwars.guildwars.core.files;

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
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/core", "config.yml");

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
        // Teleporting title
        configFile.createSection("teleporting title");
        ConfigurationSection teleportingTitlesSection = configFile.getConfigurationSection("teleporting title");
        teleportingTitlesSection.addDefault("bar length", 40);
        teleportingTitlesSection.addDefault("fadeIn", 0);
        teleportingTitlesSection.addDefault("stay", 2);
        teleportingTitlesSection.addDefault("fadeOut", 0);
        // Chat channels
        configFile.createSection("chat channels");
        ConfigurationSection chatChannelsSection = configFile.getConfigurationSection("chat channels");
        chatChannelsSection.addDefault("GUILD", "&2[G] <display name> &a<msg>"); // Can use name instead to format the name
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
