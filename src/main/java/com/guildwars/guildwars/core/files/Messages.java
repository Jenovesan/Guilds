package com.guildwars.guildwars.core.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Messages {

    private static File file;
    private static FileConfiguration messagesFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/core", "messages.yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating core/messages.yml file");
            }
        }
        messagesFile = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
        messagesFile.options().copyDefaults(true);
        save();
    }

    public static void loadDefaults() {
        messagesFile.createSection("teleporting");
        ConfigurationSection teleportingSection = messagesFile.getConfigurationSection("teleporting");
        teleportingSection.addDefault("title.title", "&aTeleporting...");
        teleportingSection.addDefault("title.bar charged prefix", "&a");
        teleportingSection.addDefault("title.bar uncharged prefix", "&7");
        teleportingSection.addDefault("title.bar", "▎");
        teleportingSection.addDefault("message.teleporting", "&aTeleporting...");
        teleportingSection.addDefault("message.teleported", "&aTeleported!");
        teleportingSection.addDefault("message.cancelled", "&cTeleport Cancelled");
    }

    public static String getMsg(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(get().getString(path)));
    }

    public static FileConfiguration get() {
        return messagesFile;
    }

    public static void save() {
        try {
            messagesFile.save(file);
        } catch (IOException e) {
            System.out.println("Error when saving core/messages.yml file");
        }
    }

    public static void reload() {
        messagesFile = YamlConfiguration.loadConfiguration(file);
    }
}
