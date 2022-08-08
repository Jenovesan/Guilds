package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            commandsSection.addDefault("guild rank too low", "&cYou must be at least &4%INPUT% &cto perform this command");
            commandsSection.addDefault("not in guild", "&cYou must be in a guild to perform this command");
            //create
            commandsSection.addDefault("create.description", "&2Creates your own guild");
            commandsSection.addDefault("create.usage", "&2/g create <name> <description>");
            commandsSection.addDefault("create.creation", "&aYou have created a new guild!");
            commandsSection.addDefault("create.already in guild", "&cYou must leave your guild to create your own");
            //help
            commandsSection.addDefault("help.title", "&2&nGuild Commands");
            commandsSection.addDefault("help.color", "&a");
            //who & show
            commandsSection.addDefault("who.description", "&2Returns that guild's info");
            commandsSection.addDefault("who.usage", "&2/g who,show <name>");
            //invite
            commandsSection.addDefault("invite.description", "&2/Invite a player to your guild");
            commandsSection.addDefault("invite.usage", "&2/g invite <name>");
            commandsSection.addDefault("invite.invitee not found", "&4%INPUT% &cis not a player");
            commandsSection.addDefault("invite.inviter rank to low", "&cYou Guild Rank is too low");
            commandsSection.addDefault("invite.invite expired", "&cYour invite to &4%GUILD+NAME% &chas expired");
            commandsSection.addDefault("invite.invitee in guild", "&4%TARGET_DISPLAY_NAME% &cis in a guild");
            commandsSection.addDefault("invite.invitee in inviter guild", "&4%TARGET_DISPLAY_NAME% &cis already a member of your guild");
            commandsSection.addDefault("invite.successfully invited", "&aYou invited &2%TARGET_DISPLAY_NAME% &ato your guild");
            commandsSection.addDefault("invite.invitee invite msg", "&aYou have been invited to &2%TARGET_GUILD_NAME%");
            commandsSection.addDefault("invite.already invited", "&4%TARGET_DISPLAY_NAME% &cis already invited to your guild. Use &4/g deinvite &cto remove them from your guild's invite list");
            //deinvite
            commandsSection.addDefault("deinvite.description", "&2Remove a player from your guild's invite list");
            commandsSection.addDefault("deinvite.usage", "&2/g deinvite <name>");
            commandsSection.addDefault("deinvite.deinvitee not found", "&4%INPUT% &cis not a player");
            commandsSection.addDefault("deinvite.not invited", "&4%INPUT% &cis not invited to your guild");
            commandsSection.addDefault("deinvite.successfully deinvited", "&aYou deinvited &2%TARGET_DISPLAY_NAME% &afrom your guild");
            commandsSection.addDefault("deinvite.deinvitee deinvited msg", "&cYour invite to &4%TARGET_GUILD_NAME% &chas been revoked");
            // disband
            commandsSection.addDefault("disband.description", "&2Disbands your guild");
            commandsSection.addDefault("disband.usage", "&2/g disband");
            commandsSection.addDefault("disband.successfully disbanded", "&aYou disbanded your guild");
            // join
            commandsSection.addDefault("join.description", "&2Joins a guild");
            commandsSection.addDefault("join.usage", "&2/g join <guild | player>");
            commandsSection.addDefault("join.in guild", "&cYou must leave your guild to create your own");
            commandsSection.addDefault("join.not a guild or player", "&4%INPUT% &cis not a guild or player");
            commandsSection.addDefault("join.not invited", "&cYou are not invited to &4%TARGET_GUILD_NAME%");
            commandsSection.addDefault("join.guild is full", "&4%TARGET_GUILD_NAME% &cis full");
            commandsSection.addDefault("join.successfully joined", "&aYou joined &2%TARGET_GUILD_NAME%");
            // leave
            commandsSection.addDefault("leave.description", "&2Leaves your guild");
            commandsSection.addDefault("leave.usage", "&2/g leave");
            commandsSection.addDefault("leave.is leader", "&cYou must disband your guild");
            commandsSection.addDefault("leave.successfully left", "&aYou left &2%PLAYER_GUILD_NAME%");
            // kick
            commandsSection.addDefault("kick.description", "&2Kick a player from your guild");
            commandsSection.addDefault("kick.usage", "&2/g kick <name>");
            commandsSection.addDefault("kick.kickee not found", "&4%INPUT% &cis not in your guild");
            commandsSection.addDefault("kick.guild rank not higher", "&cYour guild rank must be higher than the person you are kicking");
            commandsSection.addDefault("kick.kickee kicked msg", "&cYou have been kicked from &4%PLAYER_GUILD_NAME%");
            commandsSection.addDefault("kick.successfully kicked", "&aYou kicked &2%TARGET_DISPLAY_NAME%&a from your guild");
            // promote
            commandsSection.addDefault("promote.description", "&2Promotes a player in your guild");
            commandsSection.addDefault("promote.usage", "&2/g promote <name>");
            commandsSection.addDefault("promote.promotee not found", "&4%INPUT% &cis not in your guild");
            commandsSection.addDefault("promote.rank not high enough", "&cYour rank is not high enough to promote &4%TARGET_DISPLAY_NAME%");
            commandsSection.addDefault("promote.promotee promoted msg", "&aYou were promoted to &2%TARGET_GUILD_RANK%");
            commandsSection.addDefault("promote.successfully promoted", "&aYou promoted &2%TARGET_DISPLAY_NAME% &ato &2%TARGET_GUILD_RANK%");
            commandsSection.addDefault("promote.tried to make leader", "&cYou must use &4/g leader <name> &cto make someone your guild's leader");
            // demote
            commandsSection.addDefault("demote.description", "&2Demotes a player in your guild");
            commandsSection.addDefault("demote.usage", "&2/g demote <name>");
            commandsSection.addDefault("demote.demotee not found", "&4%INPUT% &cis not in your guild");
            commandsSection.addDefault("demote.rank not high enough", "&cYour rank is not high enough to demote &4%TARGET_DISPLAY_NAME%");
            commandsSection.addDefault("demote.demotee demoted msg", "&cYou were demoted to &4%TARGET_GUILD_RANK%");
            commandsSection.addDefault("demote.successfully demoted", "&aYou demoted &2%TARGET_DISPLAY_NAME% &ato &2%TARGET_GUILD_RANK%");
            commandsSection.addDefault("demote.cannot demote any further", "&4%TARGET_DISPLAY_NAME% &ccannot be demoted any further");
            // desc
            commandsSection.addDefault("desc.description", "&2Sets your guild's description");
            commandsSection.addDefault("desc.usage", "&2/g desc <description>");
            commandsSection.addDefault("desc.successfully set desc", "&aYou set your guild's description to: &2%INPUT%");
            // name
            commandsSection.addDefault("name.description", "&2Sets your guild's name");
            commandsSection.addDefault("name.usage", "&2/g name <name>");
            commandsSection.addDefault("name.successfully set name", "&aYou set your guild's name to: &2%INPUT%");
            // chat
            commandsSection.addDefault("chat.description", "&2Joins or leaves your guild's chat channel");
            commandsSection.addDefault("chat.usage", "&2/g chat || /g chat <message>");
            commandsSection.addDefault("chat.joined guild chat", "&aYou joined your guild's chat");
            commandsSection.addDefault("chat.left guild chat", "&aYou left your guild's chat");
            // enemy
            commandsSection.addDefault("enemy.description", "&2Enemies a guild");
            commandsSection.addDefault("enemy.usage", "&2/g enemy <guild | player>");
            commandsSection.addDefault("enemy.player not in guild", "&4%TARGET_DISPLAY_NAME% &cis not in a guild");
            commandsSection.addDefault("enemy.not a guild or player", "&4%INPUT% &cis not a guild or player");
            commandsSection.addDefault("enemy.already enemied", "&cYour guild is already enemied with &4%TARGET_GUILD_NAME%");
            commandsSection.addDefault("enemy.cannot enemy own guild", "&cYou cannot enemy your own guild");
            commandsSection.addDefault("enemy.successfully enemied", "&cYou enemied &4%TARGET_GUILD_NAME%");
            // truce
            commandsSection.addDefault("truce.description", "&2Truces a guild you are enemied with");
            commandsSection.addDefault("truce.usage", "&2/g truce <guild | player>");
            commandsSection.addDefault("truce.player not in guild", "&4%TARGET_DISPLAY_NAME% &cis not in a guild");
            commandsSection.addDefault("truce.not a guild or player", "&4%INPUT% &cis not a guild or player");
            commandsSection.addDefault("truce.not enemied", "&cYour guild is not enemied with &4%TARGET_GUILD_NAME%");
            commandsSection.addDefault("truce.successfully truced", "&aYou truced &2%TARGET_GUILD_NAME%");
            commandsSection.addDefault("truce.successfully sent truce request", "&aYou sent a truce request to &2%TARGET_GUILD_NAME%");
            commandsSection.addDefault("truce.already sent truce request", "&cYour guild has already requested a truce with &4%TARGRT_GUILD_NAME%");
            // leader
            commandsSection.addDefault("leader.description", "&2Sets a member of your guild to Leader rank");
            commandsSection.addDefault("leader.usage", "&2/g leader <name>");
            commandsSection.addDefault("leader.not leader", "&4%TARGET_DISPLAY_NAME% &cis not in a guild");
            commandsSection.addDefault("leader.new leader success msg", "&aYou have been given leadership to &2%TARGET_GUILD_NAME%");
            commandsSection.addDefault("leader.old leader success msg", "&aYou gave &4%TARGET_DISPLAY_NAME% &aleadership of your guild");
            // map
            commandsSection.addDefault("map.map construction.header", "&7&l |-=-=-=-=- &6&lN &7&l-=-=-=-=-|");
            commandsSection.addDefault("map.map construction.west", "&6W");
            commandsSection.addDefault("map.map construction.east", "&6E");
            commandsSection.addDefault("map.map construction.claim symbol", "&l■");
            commandsSection.addDefault("map.map construction.player symbol", "&a♦ ");
            commandsSection.addDefault("map.map construction.wilderness claim prefix", "&7");
            commandsSection.addDefault("map.map construction.player guild claim prefix", "&a");
            commandsSection.addDefault("map.map construction.guild colors", List.of("&9", "&c", "&d", "&e", "&b", "&6", "&f", "&2", "&3", "&5", "&1", "&8"));
            commandsSection.addDefault("map.map construction.footer", "&7&l |-=-=-=-=- &6&lS &7&l-=-=-=-=-|");
            // power
            commandsSection.addDefault("power.description", "&2Sets a member of your guild to Leader rank");
            commandsSection.addDefault("power.usage", "&2/g leader <name>");

        // Guilds
        messagesFile.createSection("guild announcements");
            ConfigurationSection guildAnnouncementsSection = messagesFile.getConfigurationSection("guild announcements");
            assert guildAnnouncementsSection != null;
            // announcements
            guildAnnouncementsSection.addDefault("disband", "&c&lYour Guild has been &4&ldisbanded!");
            guildAnnouncementsSection.addDefault("player join", "&2&l%PLAYER_DISPLAY_NAME% &a&lhas joined your guild!");
            guildAnnouncementsSection.addDefault("guild was full", "&4&l%PLAYER_DISPLAY_NAME% &c&ltried to join your guild, but your guild was full!");
            guildAnnouncementsSection.addDefault("player leave", "&4&l%PLAYER_DISPLAY_NAME% &c&lleft your guild!");
            guildAnnouncementsSection.addDefault("player kicked", "&4&l%PLAYER_DISPLAY_NAME% &c&lhas kicked &4&l%TARGET_DISPLAY_NAME% &c&lfrom your guild!");
            guildAnnouncementsSection.addDefault("description changed", "&2&l%PLAYER_DISPLAY_NAME% &a&lset your guild's description to &2&l%INPUT%!");
            guildAnnouncementsSection.addDefault("name changed", "&2&l%PLAYER_DISPLAY_NAME% &a&lset your guild's name to: &2&l%INPUT%!");
            guildAnnouncementsSection.addDefault("enemied guild", "&c&lYour guild is now enemied with &4&l%TARGET_GUILD_NAME%!");
            guildAnnouncementsSection.addDefault("guild has enemied your guild", "&4&l%TARGET_GUILD_NAME% &c&lhas enemied your guild!");
            guildAnnouncementsSection.addDefault("truced guild", "&a&lYour guild is now truced with &2&l%TARGET_GUILD_NAME%!");
            guildAnnouncementsSection.addDefault("sent truce request", "&2&l%PLAYER_DISPLAY_NAME% &a&lhas sent a truce request to &2&l%TARGET_GUILD_NAME%!");
            guildAnnouncementsSection.addDefault("received truce request", "&2&l%TARGET_GUILD_NAME% &a&lhas requested to truce with your guild!");
            guildAnnouncementsSection.addDefault("gave leadership", "&2&l%PLAYER_DISPLAY_NAME% &a&lhas given leadership to the guild to &2&l%TARGET_DISPLAY_NAME%!");


        // Guild Naming
        messagesFile.createSection("guild naming");
            ConfigurationSection guildNamingSection = messagesFile.getConfigurationSection("guild naming");
            assert guildNamingSection != null;
            // naming
            guildNamingSection.addDefault("name too long", "&cGuild name is too long");
            guildNamingSection.addDefault("name exists", "&cThat guild name is already being used");
            guildNamingSection.addDefault("name contains not legal character", "&cThat guild name contains an illegal character");
            guildNamingSection.addDefault("name blacklisted", "&cThat guild name is not allowed");
    }

    public static FileConfiguration get() {
        return messagesFile;
    }

    public static String getMsg(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(get().getString(path)));
    }

    public static String[] getStringArray(String path) {
        List<String> stringList = get().getStringList(path);
        String[] newStringArray = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            newStringArray[i] = stringList.get(i);
        }
        return newStringArray;
    }

    public static String getMsg(String path,
                                gPlayer player,
                                gPlayer targetPlayer,
                                String input) {
        String msg = get().getString(path);
        assert msg != null;
        if (player != null) {
            msg = msg.replace("%PLAYER_NAME%", player.getName());

            if (player.getPlayer() != null) {
                msg = msg.replace("%PLAYER_DISPLAY_NAME%", player.getPlayer().getDisplayName());
            }

            if (player.getGuild() != null) {
                msg = msg.replace("%PLAYER_GUILD_NAME%", player.getGuild().getName());
            }

            if (player.getGuildRank() != null) {
                msg = msg.replace("%PLAYER_GUILD_RANK%", util.formatEnum(player.getGuildRank()));
            }
        }
        if (targetPlayer != null) {
            msg = msg.replace("%TARGET_NAME%", targetPlayer.getName());

            if (targetPlayer.getPlayer() != null) {
                msg = msg.replace("%TARGET_DISPLAY_NAME%", targetPlayer.getPlayer().getDisplayName());
            }

            if (targetPlayer.getGuild() != null) {
                msg = msg.replace("%TARGET_GUILD_NAME%", targetPlayer.getGuild().getName());
            }

            if (targetPlayer.getGuildRank() != null) {
                msg = msg.replace("%TARGET_GUILD_RANK%", util.formatEnum(targetPlayer.getGuildRank()));
            }
        }

        if (input != null) {
            msg = msg.replace("%INPUT%", input);
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
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
