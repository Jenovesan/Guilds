package com.guildwars.guildwars.guilds.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Messages {

    private static File file;
    private static FileConfiguration messagesFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("GuildWars").getDataFolder() + "/guilds", "messages.yml");

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating guilds/messages.yml file");
            }
        }
        messagesFile = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
        messagesFile.options().copyDefaults(true);
        save();
    }

    public static void loadDefaults() {
        messagesFile.createSection("commands");
        //commands
        ConfigurationSection commandsSection = messagesFile.getConfigurationSection("commands");
        assert commandsSection != null;
        commandsSection.addDefault("command does not exist", "&cCommand does not exist. Try /g help for a list of guild commands");
            //create
            commandsSection.addDefault("create.description", "&2Creates your own guild");
            commandsSection.addDefault("create.usage", "&2/g create <name> <description>");
            //help
            commandsSection.addDefault("help.title", "&2&nGuild Commands");
            commandsSection.addDefault("help.color", "&a");
            //who & show
            commandsSection.addDefault("who.description", "&2Returns that guild's info");
            commandsSection.addDefault("who.usage", "&2/g who,show <name>");
    }

    public static FileConfiguration get() {
        return messagesFile;
    }

    public static String getMsg(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path));
    }

    public static void save() {
        try {
            messagesFile.save(file);
        } catch (IOException e) {
            System.out.println("Error when saving guilds/messages.yml file");
        }
    }

    public static void reload() {
        messagesFile = YamlConfiguration.loadConfiguration(file);
    }
}
