package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerData {
    private static File file;
    private static FileConfiguration dataFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/guilds", "player_data.yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating guilds/player_data.yml file");
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
            System.out.println("Error when saving guilds/player_data.yml file");
        }
    }

    public static void reload() {
        dataFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveAllPlayerData() {
        for (gPlayer player : gPlayers.getAllGPlayers()) {
            String uuid = String.valueOf(player.getUUID());
            get().createSection(uuid);
            ConfigurationSection playerSection = get().getConfigurationSection(uuid);
            assert playerSection != null;
            playerSection.set("guildId", player.getGuildId());
            if (player.getGuildRank() != null) {
                playerSection.set("guildRank", player.getGuildRank().name());
            }
            playerSection.set("name", player.getName());
            playerSection.set("power", player.getPower());
            playerSection.set("powerChangeTime", player.getPowerChangeTime());
        }
        save();
    }
}
