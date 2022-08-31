package com.guildwars.guildwars.guilds;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ObjectDataManager<T> {

    String folderName;
    File dataFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/" + folderName);
    String dataFolderPath;

    public ObjectDataManager(String folderName) {

        this.folderName = folderName;
        this.dataFolderPath = dataFolder.getAbsolutePath();

        if (!dataFolder.exists()) {
            System.err.println("Unable to create " + folderName + " folder");
        }
    }

    protected void saveRaw(String fileName, HashMap<String, Object> data) {
        File rawFile = new File(dataFolderPath + "/" + fileName + ".yml");
        // File does not exist
        if (!rawFile.exists()) {
            // Create rawFile
            try {
                rawFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Unable to create " + fileName + ".yml");
            }
        }

        // Apply data
        FileConfiguration file = YamlConfiguration.loadConfiguration(rawFile);

        file.set("", data);

        // Save data
        try {
            file.save(rawFile);
        } catch (IOException e) {
            System.err.println("Unable to save " + fileName + ".yml");
        }
    }

    public abstract void save(T obj);

    public List<FileConfiguration> getAllData() {
        List<FileConfiguration> files = new ArrayList<>();

        for (File rawFile : dataFolder.listFiles()) {
            FileConfiguration file = YamlConfiguration.loadConfiguration(rawFile);
            files.add(file);
        }
        return files;
    }

}
