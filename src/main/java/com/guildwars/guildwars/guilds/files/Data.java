package com.guildwars.guildwars.guilds.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Data {
    private static File file;
    private static FileConfiguration dataFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/guilds", "data.yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating guilds/messages.yml file");
            }
        }
        dataFile = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
        dataFile.options().copyDefaults(true);
        save();
    }

    public static void loadDefaults() {}

    public static FileConfiguration get() {
        return dataFile;
    }

    public static void save() {
        try {
            dataFile.save(file);
        } catch (IOException e) {
            System.out.println("Error when saving guilds/messages.yml file");
        }
    }

    public static void reload() {
        dataFile = YamlConfiguration.loadConfiguration(file);
    }
}
