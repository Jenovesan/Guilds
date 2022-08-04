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
            commandsSection.addDefault("guild rank too low", "&cYou must be at least &4<rank> &cto perform this command");
            commandsSection.addDefault("not in guild", "&cYou must be in a guild to perform this command");
            //create
            commandsSection.addDefault("create.description", "&2Creates your own guild");
            commandsSection.addDefault("create.usage", "&2/g create <name> <description>");
            commandsSection.addDefault("create.creation", "&aYou have created a new guild!");
            commandsSection.addDefault("create.already in guild", "&cYou must leave your guild to create your own");
            commandsSection.addDefault("create.guild name too long", "&cGuild name is too long");
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
            commandsSection.addDefault("invite.inviter rank to low", "&cYou rank is too low");
            commandsSection.addDefault("invite.invite expired", "&cYour invite to &4<guild> &chas expired");
            commandsSection.addDefault("invite.invitee in guild", "&4<name> &cis in a guild");
            commandsSection.addDefault("invite.invitee in inviter guild", "&4<name> &cis already a member of your guild");
            commandsSection.addDefault("invite.successfully invited", "&aYou invited &2<name> &ato your guild");
            commandsSection.addDefault("invite.invitee invite msg", "&aYou have been invited to &2<guild>");
            commandsSection.addDefault("invite.already invited", "&4<name> &cis already invited to your guild. Use &4/g deinvite &cto remove them from your guild's invite list");
            //deinvite
            commandsSection.addDefault("deinvite.description", "&2Remove a player from your guild's invite list");
            commandsSection.addDefault("deinvite.usage", "&2/g deinvite <name>");
            commandsSection.addDefault("deinvite.deinvitee not found", "&4<input> &cis not a player");
            commandsSection.addDefault("deinvite.not invited", "&4<input> &cis not invited to your guild");
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
            commandsSection.addDefault("join.not a guild or player", "&4<input> &cis not a guild or player");
            commandsSection.addDefault("join.not invited", "&cYou are not invited to &4<name>");
            commandsSection.addDefault("join.guild is full", "&4<name> &cis full");
            commandsSection.addDefault("join.successfully joined", "&aYou joined &2<name>");
            // leave
            commandsSection.addDefault("leave.description", "&2Leaves your guild");
            commandsSection.addDefault("leave.usage", "&2/g leave");
            commandsSection.addDefault("leave.is leader", "&cYou must disband your guild");
            commandsSection.addDefault("leave.successfully left", "&aYou left &2<name>");
            // kick
            commandsSection.addDefault("kick.description", "&2Kick a player from your guild");
            commandsSection.addDefault("kick.usage", "&2/g kick <name>");
            commandsSection.addDefault("kick.kickee not found", "&4<input> &cis not in your guild");
            commandsSection.addDefault("kick.guild rank not higher", "&cYour guild rank must be higher than the person you are kicking");
            commandsSection.addDefault("kick.kickee kicked msg", "&cYou have been kicked from &4<name>");
            commandsSection.addDefault("kick.successfully kicked", "&aYou kicked &2<name>&a from your guild");
            // promote
            commandsSection.addDefault("promote.description", "&2Promotes a player in your guild");
            commandsSection.addDefault("promote.usage", "&2/g promote <name>");
            commandsSection.addDefault("promote.promotee not found", "&4<input> &cis not in your guild");
            commandsSection.addDefault("promote.rank not high enough", "&cYour rank is not high enough to promote &4<name>");
            commandsSection.addDefault("promote.promotee promoted msg", "&aYou were promoted to &2<rank>");
            commandsSection.addDefault("promote.successfully promoted", "&aYou promoted &2<name> &ato &2<rank>");
            commandsSection.addDefault("promote.tried to make leader", "&cYou must use &4/g leader <name> &cto make someone your guild's leader");
            // demote
            commandsSection.addDefault("demote.description", "&2Demotes a player in your guild");
            commandsSection.addDefault("demote.usage", "&2/g demote <name>");
            commandsSection.addDefault("demote.demotee not found", "&4<input> &cis not in your guild");
            commandsSection.addDefault("demote.rank not high enough", "&cYour rank is not high enough to demote &4<name>");
            commandsSection.addDefault("demote.demotee demoted msg", "&cYou were demoted to &4<rank>");
            commandsSection.addDefault("demote.successfully demoted", "&aYou demoted &2<name> &ato &2<rank>");
            commandsSection.addDefault("demote.cannot demote any further", "&4<name> &ccannot be demoted any further");
            // desc
            commandsSection.addDefault("desc.description", "&2Sets your guild's description");
            commandsSection.addDefault("desc.usage", "&2/g desc <description>");
            commandsSection.addDefault("desc.successfully set desc", "&aYou set your guild's description to: &2<description>");
            // name
            commandsSection.addDefault("name.description", "&2Sets your guild's name");
            commandsSection.addDefault("name.usage", "&2/g name <name>");
            commandsSection.addDefault("name.successfully set name", "&aYou set your guild's name to: &2<name>");
            commandsSection.addDefault("name.guild name exists", "&cThat guild name is already being used");
            commandsSection.addDefault("name.guild name too long", "&cGuild name is too long");
            // chat
            commandsSection.addDefault("chat.description", "&2Joins or leaves your guild's chat channel");
            commandsSection.addDefault("chat.usage", "&2/g chat || /g chat <message>");
            commandsSection.addDefault("chat.joined guild chat", "&aYou joined your guild's chat");
            commandsSection.addDefault("chat.left guild chat", "&aYou left your guild's chat");
            // enemy
            commandsSection.addDefault("enemy.description", "&2Enemies a guild");
            commandsSection.addDefault("enemy.usage", "&2/g enemy <guild | player>");
            commandsSection.addDefault("enemy.player not in guild", "&4<name> &cis not in a guild");
            commandsSection.addDefault("enemy.not a guild or player", "&4<input> &cis not a guild or player");
            commandsSection.addDefault("enemy.already enemied", "&cYour guild is already enemied with &4<name>");
            commandsSection.addDefault("enemy.cannot enemy own guild", "&cYou cannot enemy your own guild");
            commandsSection.addDefault("enemy.successfully enemied", "&cYou enemied &4<name>");
            // truce
            commandsSection.addDefault("truce.description", "&Truces a guild you are enemied with");
            commandsSection.addDefault("truce.usage", "&2/g truce <guild | player>");
            commandsSection.addDefault("truce.player not in guild", "&4<name> &cis not in a guild");
            commandsSection.addDefault("truce.not a guild or player", "&4<input> &cis not a guild or player");
            commandsSection.addDefault("truce.not enemied", "&cYour guild is not enemied with &4<name>");
            commandsSection.addDefault("truce.successfully truced", "&aYou truced &2<name>");
            commandsSection.addDefault("truce.successfully sent truce request", "&aYou sent a truce request to &2<name>");
            commandsSection.addDefault("truce.already sent truce request", "&cYour guild has already requested a truce with &4<name>");

        // Guilds
        messagesFile.createSection("guilds");
            ConfigurationSection guildsSection = messagesFile.getConfigurationSection("guilds");
            assert guildsSection != null;
            // announcements
            guildsSection.addDefault("announcements.disband", "&c&lYour Guild has been &4&ldisbanded!");
            guildsSection.addDefault("announcements.player join", "&2&l<name> &a&lhas joined your guild!");
            guildsSection.addDefault("announcements.guild was full", "&4&l<name> &c&ltried to join your guild, but your guild was full!");
            guildsSection.addDefault("announcements.player leave", "&4&l<name> &c&lleft your guild!");
            guildsSection.addDefault("announcements.player kicked", "&4&l<kicker> &c&lhas kicked &4&l<kickee> &c&lfrom your guild!");
            guildsSection.addDefault("announcements.description changed", "&2&l<name> &a&lset your guild's description to: &2&l<description>");
            guildsSection.addDefault("announcements.name changed", "&2&l<player name> &a&lset your guild's name to: &2&l<name>");
            guildsSection.addDefault("announcements.enemied guild", "&c&lYour guild is now enemied with &4&l<name>"); // Can use player name to say who enemied the guild
            guildsSection.addDefault("announcements.guild has enemied your guild", "&4&l<name> &c&lhas enemied your guild");
            guildsSection.addDefault("announcements.truced guild", "&a&lYour guild is now truced with &2&l<name>");
            guildsSection.addDefault("announcements.sent truce request", "&2&l<name> &a&lhas sent a truce request to &2&l<guild>");
            guildsSection.addDefault("announcements.received truce request", "&2&l<name> &a&lhas requested to truce with your guild");
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
