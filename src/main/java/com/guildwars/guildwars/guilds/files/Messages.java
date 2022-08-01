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

        // Commands
        messagesFile.createSection("commands");
            ConfigurationSection commandsSection = messagesFile.getConfigurationSection("commands");
            assert commandsSection != null;
            commandsSection.addDefault("command does not exist", "&cCommand does not exist. Try /g help for a list of guild commands");
            commandsSection.addDefault("too few arguments given", "&cToo few arguments were given. Try /g help for command usage");
            commandsSection.addDefault("guild rank too low", "&cYour guild rank must be higher to perform this command");
            //create
            commandsSection.addDefault("create.description", "&2Creates your own guild");
            commandsSection.addDefault("create.usage", "&2/g create <name> <description>");
            commandsSection.addDefault("create.creation", "&aYou have created a new guild!");
            commandsSection.addDefault("create.already in guild", "&cYou must leave your guild to create your own");
            commandsSection.addDefault("create.guild name too long", "&cYour guild name is too long");
            commandsSection.addDefault("create.guild name exists", "&cThat guild name is already being used");
            //help
            commandsSection.addDefault("help.title", "&2&nGuild Commands");
            commandsSection.addDefault("help.color", "&a");
            //who & show
            commandsSection.addDefault("who.description", "&2Returns that guild's info");
            commandsSection.addDefault("who.usage", "&2/g who,show <name>");
            //invite
            commandsSection.addDefault("invite.description", "&2/Invite a player to your guild");
            commandsSection.addDefault("invite.usage", "&2/g invite <name>");
            commandsSection.addDefault("invite.invitee not found", "&4<input> &cis not a player");
            commandsSection.addDefault("invite.inviter not in guild", "&cYou are not in a guild");
            commandsSection.addDefault("invite.inviter rank to low", "&cYou rank is too low");
            commandsSection.addDefault("invite.invite expired", "&cYour invite to &4<guild> &chas expired");
            commandsSection.addDefault("invite.invitee in guild", "&4<name> &cis in a guild");
            commandsSection.addDefault("invite.successfully invited", "&aYou invited &2<name> &ato your guild");
            commandsSection.addDefault("invite.invitee invite msg", "&aYou have been invited to &2<guild>");
            commandsSection.addDefault("invite.already invited", "&4<name> &cis already invited to your guild. Use &4/g deinvite &cto remove them from your guild's invite list");
            //deinvite
            commandsSection.addDefault("deinvite.description", "&2Remove a player from your guild's invite list");
            commandsSection.addDefault("deinvite.usage", "&2/g deinvite <name>");
            commandsSection.addDefault("deinvite.deinvitee not found", "&4<input> &cis not a player");
            commandsSection.addDefault("deinvite.not invited", "&4<input> &cis not invited to your guild");
            commandsSection.addDefault("deinvite.deinviter not in guild", "&cYou are not in a guild");
            commandsSection.addDefault("deinvite.successfully deinvited", "&aYou deinvited &2<name> &afrom your guild");
            commandsSection.addDefault("deinvite.deinvitee deinvited msg", "&cYour invite to &4<name> &chas been revoked");
            // disband
            commandsSection.addDefault("disband.description", "&2Disbands your guild");
            commandsSection.addDefault("disband.usage", "&2/g disband");
            commandsSection.addDefault("disband.successfully disbanded", "&aYou disbanded your guild");
            // join
            commandsSection.addDefault("join.description", "&2Joins a guild");
            commandsSection.addDefault("join.usage", "&2/g join <guild | player>");
            commandsSection.addDefault("join.in guild", "&cYou must leave your guild to create your own");
            commandsSection.addDefault("join.player not in guild", "&4<name> &cis not in a guild");
            commandsSection.addDefault("join.not a guild or player", "&4<input> &cis not a guild or player");
            commandsSection.addDefault("join.not invited", "&cYou are not invited to &4<name>");
            commandsSection.addDefault("join.guild is full", "&4<name> &cis full");
            commandsSection.addDefault("join.successfully joined", "&aYou joined &2<name>");
            // leave
            commandsSection.addDefault("leave.description", "&2Leaves your guild");
            commandsSection.addDefault("leave.usage", "&2/g leave");
            commandsSection.addDefault("leave.not in guild", "&cYou are not in a guild");
            commandsSection.addDefault("leave.is leader", "&cYou must disband your guild");
            commandsSection.addDefault("leave.successfully left", "&aYou left &2<name>");

        // Guilds
        messagesFile.createSection("guilds");
            ConfigurationSection guildsSection = messagesFile.getConfigurationSection("guilds");
            assert guildsSection != null;
            // announcements
            guildsSection.addDefault("announcements.disband", "&c&lYour Guild has been &4&ldisbanded!");
            guildsSection.addDefault("announcements.player join", "&2&l<name> &a&ljoined your guild!");
            guildsSection.addDefault("announcements.guild was full", "&4&l<name> &c&ltried to join your guild, but your guild was full!");
            guildsSection.addDefault("announcements.player leave", "&4&l<name> &c&lleft your guild!");


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