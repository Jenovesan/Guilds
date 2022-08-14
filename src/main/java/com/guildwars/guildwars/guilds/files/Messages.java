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

import java.io.File;
import java.io.IOException;
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
            commandsSection.addDefault("guild rank too low", "&cYou must be at least &4%RANK% &cto perform this command");
            commandsSection.addDefault("not in guild", "&cYou must be in a guild to perform this command");
            commandsSection.addDefault("player not found", "&cNo player &4%INPUT% &ccould be found");
            commandsSection.addDefault("player not in your guild", "&4%PLAYER% &cis not in your guild");
            commandsSection.addDefault("not a guild or player", "&4%INPUT% &cis not a guild or player");
            //create
            commandsSection.addDefault("create.description", "&2Creates your own guild");
            commandsSection.addDefault("create.usage", "&2/g create <name> <description>");
            commandsSection.addDefault("create.creation", "&aYou have created a the guild &2%GUILD%");
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
            commandsSection.addDefault("invite.inviter rank to low", "&cYou Guild Rank is too low");
            commandsSection.addDefault("invite.invite expired", "&cYour invite to &4%GUILD% &chas expired");
            commandsSection.addDefault("invite.invitee in guild", "&4%PLAYER% &cis in a guild");
            commandsSection.addDefault("invite.invitee in inviter guild", "&4%PLAYER% &cis already a member of your guild");
            commandsSection.addDefault("invite.successfully invited", "&aYou invited &2%PLAYER% &ato your guild");
            commandsSection.addDefault("invite.invitee invite msg", "&aYou have been invited to &2%GUILD%");
            commandsSection.addDefault("invite.already invited", "&4%PLAYER% &cis already invited to your guild. Use &4/g deinvite &cto remove them from your guild's invite list");
            //deinvite
            commandsSection.addDefault("deinvite.description", "&2Remove a player from your guild's invite list");
            commandsSection.addDefault("deinvite.usage", "&2/g deinvite <name>");
            commandsSection.addDefault("deinvite.not invited", "&4%PLAYER% &cis not invited to your guild");
            commandsSection.addDefault("deinvite.successfully deinvited", "&aYou deinvited &2%PLAYER% &afrom your guild");
            commandsSection.addDefault("deinvite.deinvitee deinvited msg", "&cYour invite to &4%GUILD% &chas been revoked");
            // disband
            commandsSection.addDefault("disband.description", "&2Disbands your guild");
            commandsSection.addDefault("disband.usage", "&2/g disband");
            commandsSection.addDefault("disband.successfully disbanded", "&aYou disbanded your guild &2%GUILD%");
            // join
            commandsSection.addDefault("join.description", "&2Joins a guild");
            commandsSection.addDefault("join.usage", "&2/g join <guild | player>");
            commandsSection.addDefault("join.in guild", "&cYou must leave your guild to create your own");
            commandsSection.addDefault("join.not invited", "&cYou are not invited to &4%GUILD%");
            commandsSection.addDefault("join.guild is full", "&4%GUILD% &cis full");
            commandsSection.addDefault("joiner.player not in guild", "&4%PLAYER% &cis not in a guild");
            commandsSection.addDefault("join.successfully joined", "&aYou joined &2%GUILD%");
            // leave
            commandsSection.addDefault("leave.description", "&2Leaves your guild");
            commandsSection.addDefault("leave.usage", "&2/g leave");
            commandsSection.addDefault("leave.is leader", "&cYou must disband your guild or give leadership to leave");
            commandsSection.addDefault("leave.successfully left", "&aYou left &2%GUILD%");
            // kick
            commandsSection.addDefault("kick.description", "&2Kick a player from your guild");
            commandsSection.addDefault("kick.usage", "&2/g kick <name>");
            commandsSection.addDefault("kick.guild rank not higher", "&cYour guild rank must be higher than the person you are kicking");
            commandsSection.addDefault("kick.kickee kicked msg", "&cYou have been kicked from &4%GUILD%");
            commandsSection.addDefault("kick.successfully kicked", "&aYou kicked &2%PLAYER%&a from your guild");
            // promote
            commandsSection.addDefault("promote.description", "&2Promotes a player in your guild");
            commandsSection.addDefault("promote.usage", "&2/g promote <name>");
            commandsSection.addDefault("promote.rank not high enough", "&cYour rank is not high enough to promote &4%PLAYER%");
            commandsSection.addDefault("promote.promotee promoted msg", "&aYou were promoted to &2%RANK%");
            commandsSection.addDefault("promote.successfully promoted", "&aYou promoted &2%PLAYER% &ato &2%RANK%");
            commandsSection.addDefault("promote.tried to make leader", "&cYou must use &4/g leader <name> &cto make someone your guild's leader");
            // demote
            commandsSection.addDefault("demote.description", "&2Demotes a player in your guild");
            commandsSection.addDefault("demote.usage", "&2/g demote <name>");
            commandsSection.addDefault("demote.rank not high enough", "&cYour rank is not high enough to demote &4%PLAYER%");
            commandsSection.addDefault("demote.demotee demoted msg", "&cYou were demoted to &4%RANK%");
            commandsSection.addDefault("demote.successfully demoted", "&aYou demoted &2%PLAYER% &ato &2%RANK%");
            commandsSection.addDefault("demote.cannot demote any further", "&4%PLAYER% &ccannot be demoted any further");
            // desc
            commandsSection.addDefault("desc.description", "&2Sets your guild's description");
            commandsSection.addDefault("desc.usage", "&2/g desc <description>");
            commandsSection.addDefault("desc.successfully set desc", "&aYou set your guild's description to: &2%INPUT%");
            // name
            commandsSection.addDefault("name.description", "&2Sets your guild's name");
            commandsSection.addDefault("name.usage", "&2/g name <name>");
            commandsSection.addDefault("name.successfully set name", "&aYou set your guild's name to &2%INPUT%");
            // chat
            commandsSection.addDefault("chat.description", "&2Joins or leaves your guild's chat channel");
            commandsSection.addDefault("chat.usage", "&2/g chat || /g chat <message>");
            commandsSection.addDefault("chat.joined guild chat", "&aYou joined your guild's chat");
            commandsSection.addDefault("chat.left guild chat", "&aYou left your guild's chat");
            // enemy
            commandsSection.addDefault("enemy.description", "&2Enemies a guild");
            commandsSection.addDefault("enemy.usage", "&2/g enemy <guild | player>");
            commandsSection.addDefault("enemy.player not in guild", "&4%PLAYER% &cis not in a guild");
            commandsSection.addDefault("enemy.already enemied", "&cYour guild is already enemied with &4%GUILD%");
            commandsSection.addDefault("enemy.cannot enemy own guild", "&cYou cannot enemy your own guild");
            commandsSection.addDefault("enemy.successfully enemied", "&cYou enemied &4%GUILD%");
            // truce
            commandsSection.addDefault("truce.description", "&2Truces a guild you are enemied with");
            commandsSection.addDefault("truce.usage", "&2/g truce <guild | player>");
            commandsSection.addDefault("truce.player not in guild", "&4%PLAYER% &cis not in a guild");
            commandsSection.addDefault("truce.not enemied", "&cYour guild is not enemied with &4%GUILD%");
            commandsSection.addDefault("truce.successfully truced", "&aYou truced &2%GUILD%");
            commandsSection.addDefault("truce.successfully sent truce request", "&aYou sent a truce request to &2%GUILD%");
            commandsSection.addDefault("truce.already sent truce request", "&cYour guild has already requested a truce with &4%GUILD%");
            // leader
            commandsSection.addDefault("leader.description", "&2Sets a member of your guild to Leader rank");
            commandsSection.addDefault("leader.usage", "&2/g leader <name>");
            commandsSection.addDefault("leader.new leader success msg", "&aYou have been given leadership of &2%GUILD%");
            commandsSection.addDefault("leader.old leader success msg", "&aYou gave &4%PLAYER% &aleadership of %GUILD%");
            commandsSection.addDefault("leader.new leader is sender", "&cYou are already the leader of your guild");
            // map
            commandsSection.addDefault("map.description", "&2Displays a map of nearby guild claims");
            commandsSection.addDefault("map.usage", "&2/g map <?auto|on|off?>");
            commandsSection.addDefault("map.invalid syntax", "&cInvalid syntax. Use /g map <?auto?>");
            commandsSection.addDefault("map.map construction.header", "&7&l |-=-=-=-=- &6&lN &7&l-=-=-=-=-|");
            commandsSection.addDefault("map.map construction.west", "&6W");
            commandsSection.addDefault("map.map construction.east", "&6E");
            commandsSection.addDefault("map.map construction.claim symbol", "■ ");
            commandsSection.addDefault("map.map construction.player symbol", "&a♦ ");
            commandsSection.addDefault("map.map construction.wilderness claim prefix", "&7");
            commandsSection.addDefault("map.map construction.player guild claim prefix", "&a");
            commandsSection.addDefault("map.map construction.guild colors", List.of("&9", "&c", "&d", "&e", "&b", "&6", "&f", "&2", "&3", "&5", "&1", "&8"));
            commandsSection.addDefault("map.map construction.footer with guilds", "&7&l |-=-=-=-=- &6&lS &7&l-=-=-=-=-|\n&7Guilds: %INPUT%");
            commandsSection.addDefault("map.map construction.footer without guilds", "&7&l |-=-=-=-=- &6&lS &7&l-=-=-=-=-|");
            commandsSection.addDefault("map.map construction.guilds list delimiter", "&7, ");
            // power
            commandsSection.addDefault("power.description", "&2Returns your guild's power and max power");
            commandsSection.addDefault("power.usage", "&2/g power");
            commandsSection.addDefault("power.power msg", "&2Guild Power: &a%INPUT%");
            // claim
            commandsSection.addDefault("claim.description", "&2Claim a chunk for your guild");
            commandsSection.addDefault("claim.usage", "&2/g claim <?radius?>");
            commandsSection.addDefault("claim.cannot claim in world", "&cYou cannot claim land in this world");
            commandsSection.addDefault("claim.successfully claimed single chunk", "&aYou claimed land for your guild");
            commandsSection.addDefault("claim.invalid radius", "&4%INPUT% &cis not a valid radius");
            commandsSection.addDefault("claim.successfully claimed multiple chunks", "&aYou claimed &2%AMOUNT% &achunks for your guild");
            commandsSection.addDefault("claim.will not have enough power", "&cYour guild will not have enough power to claim that many chunks");
            // unclaim
            commandsSection.addDefault("unclaim.description", "&2Unclaim a chunk from your guild");
            commandsSection.addDefault("unclaim.usage", "&2/g unclaim <?radius?>");
            commandsSection.addDefault("unclaim.cannot claim in world", "&cYou cannot claim or unclaim land in this world");
            commandsSection.addDefault("unclaim.chunk not owned by guild", "&cYour guild does not own this chunk");
            commandsSection.addDefault("unclaim.successfully unclaimed single chunk", "&aYou unclaimed land from your guild");
            commandsSection.addDefault("unclaim.successfully unclaimed multiple chunks", "&aYou unclaimed &2%AMOUNT% &achunks for your guild");
            commandsSection.addDefault("unclaim.invalid radius", "&4%INPUT% &cis not a valid radius");
            commandsSection.addDefault("unclaim.radius too big", "&4%AMOUNT% &cis too big of an unclaim radius");
            // unclaimall
            commandsSection.addDefault("unclaimall.description", "&2Unclaim all of your guild's land");
            commandsSection.addDefault("unclaimall.usage", "&2/g unclaimall");
            commandsSection.addDefault("unclaimall.successfully unclaimed all", "&aYou unclaimed all your guild's land");
            // autoclaim
            commandsSection.addDefault("autoclaim.description", "&2Claim each new chunk you move into");
            commandsSection.addDefault("autoclaim.usage", "&2/g autoclaim");
            commandsSection.addDefault("autoclaim.cannot claim in world", "&cYou cannot claim land in this world");

        // Guilds
        messagesFile.createSection("guild announcements");
            ConfigurationSection guildAnnouncementsSection = messagesFile.getConfigurationSection("guild announcements");
            assert guildAnnouncementsSection != null;
            // announcements
            guildAnnouncementsSection.addDefault("disband", "&c&lYour Guild has been &4&ldisbanded!");
            guildAnnouncementsSection.addDefault("player join", "&2&l%PLAYER% &a&lhas joined your guild!");
            guildAnnouncementsSection.addDefault("guild was full", "&4&l%PLAYER% &c&ltried to join your guild, but your guild was full!");
            guildAnnouncementsSection.addDefault("player leave", "&4&l%PLAYER% &c&lleft your guild!");
            guildAnnouncementsSection.addDefault("player kicked", "&4&l%PLAYER1% &c&lhas kicked &4&l%PLAYER2% &c&lfrom your guild!");
            guildAnnouncementsSection.addDefault("description changed", "&2&l%PLAYER% &a&lset your guild's description to &2&l%INPUT%!");
            guildAnnouncementsSection.addDefault("name changed", "&2&l%PLAYER% &a&lset your guild's name to: &2&l%INPUT%!");
            guildAnnouncementsSection.addDefault("enemied guild", "&c&lYour guild is now enemied with &4&l%GUILD%!");
            guildAnnouncementsSection.addDefault("guild has enemied your guild", "&4&l%GUILD% &c&lhas enemied your guild!");
            guildAnnouncementsSection.addDefault("truced guild", "&a&lYour guild is now truced with &2&l%GUILD%!");
            guildAnnouncementsSection.addDefault("sent truce request", "&2&l%PLAYER% &a&lhas sent a truce request to &2&l%GUILD%!");
            guildAnnouncementsSection.addDefault("received truce request", "&2&l%GUILD% &a&lhas requested to truce with your guild!");
            guildAnnouncementsSection.addDefault("gave leadership", "&2&l%PLAYER1% &a&lhas given leadership to the guild to &2&l%PLAYER2%!");
            guildAnnouncementsSection.addDefault("claimed land", "&2&l%PLAYER% &a&lhas claimed land!");
            guildAnnouncementsSection.addDefault("unclaimed land", "&4&l%PLAYERE% &c&lhas unclaimed land!");
            guildAnnouncementsSection.addDefault("unclaimed all", "&4&l%PLAYER% &c&lhas unclaimed all your guild's land!");
            guildAnnouncementsSection.addDefault("overclaimed", "&4&l%GUILD% &c&lhas overclaimed some of your land!");


        // Guild Naming
        messagesFile.createSection("guild naming");
            ConfigurationSection guildNamingSection = messagesFile.getConfigurationSection("guild naming");
            assert guildNamingSection != null;
            // naming
            guildNamingSection.addDefault("name too long", "&4%INPUT% &is too long of a name");
            guildNamingSection.addDefault("name exists", "&4%INPUT% &cis already being used");
            guildNamingSection.addDefault("name contains not legal character", "&cThat guild name cannot contain the character &4%CHAR%");
            guildNamingSection.addDefault("name blacklisted", "&cThat guild name is not allowed");

        // Autoclaiming
        messagesFile.createSection("autoclaiming");
        ConfigurationSection autoclaimingSection = messagesFile.getConfigurationSection("autoclaiming");
        assert autoclaimingSection != null;
        autoclaimingSection.addDefault("enabled", "&aYou have &2enabled &aautoclaiming");
        autoclaimingSection.addDefault("disabled", "&cYou have &4disabled &cautoclaiming");

        // Map auto
        messagesFile.createSection("map auto");
        ConfigurationSection mapAutoSection = messagesFile.getConfigurationSection("map auto");
        assert mapAutoSection != null;
        mapAutoSection.addDefault("enabled", "&aYou have &2enabled &amap auto updating");
        mapAutoSection.addDefault("disabled", "&cYou have &4disabled &cmap auto updating");

        // Claiming
        messagesFile.createSection("claiming");
        ConfigurationSection claimingSection = messagesFile.getConfigurationSection("claiming");
        assert claimingSection != null;
        claimingSection.addDefault("not enough power", "&cYour guild does not have enough power to claim more land");
        claimingSection.addDefault("claiming own land", "&cYour guild already owns this land");
        claimingSection.addDefault("cannot overclaim because claim surrounded", "&cYou can only overclaim land that is not completely surrounded by the guild's claims");
        claimingSection.addDefault("not overclaimable", "&4%GUILD% &cis not overclaimable");
        claimingSection.addDefault("no connecting claim", "&cYou can only claim land that is connected to your land");
    }

    public static FileConfiguration get() {
        return messagesFile;
    }

    public static String getMsg(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(get().getString(path)));
    }

    public static String getMsg(String path, GuildRank rank) {
        return getMsg(path).replace("%RANK%", util.formatEnum(rank));
    }

    public static String getMsg(String path, Guild guild) {
        return getMsg(path).replace("%GUILD%", guild.getName());
    }

    public static String getMsg(String path, int amount) {
        return getMsg(path).replace("%AMOUNT%", String.valueOf(amount));
    }

    public static String getMsg(String path, String input) {
        return getMsg(path).replace("%INPUT%", input);
    }

    public static String getMsg(String path, gPlayer player) {
        return getMsg(path).replace("%PLAYER%", player.getName());
    }

    public static String getMsg(String path, gPlayer player, GuildRank rank) {
        return getMsg(path).replace("%PLAYER%", player.getName()).replace("%RANK%", util.formatEnum(rank));
    }

    public static String getMsg(String path, gPlayer player, Guild guild) {
        return getMsg(path).replace("%PLAYER%", player.getName()).replace("%GUILD%", guild.getName());
    }

    public static String getMsg(String path, Character character) {
        return getMsg(path).replace("%CHAR%", String.valueOf(character));
    }

    public static String getMsg(String path, gPlayer player, String input) {
        return getMsg(path).replace("%PLAYER%", player.getName()).replace("%INPUT%", input);
    }

    public static String getMsg(String path, gPlayer player1, gPlayer player2) {
        return getMsg(path).replace("%PLAYER1%", player1.getName()).replace("%PLAYER2%", player2.getName());
    }

    public static String[] getStringArray(String path) {
        List<String> stringList = get().getStringList(path);
        String[] newStringArray = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            newStringArray[i] = ChatColor.translateAlternateColorCodes('&', stringList.get(i));
        }
        return newStringArray;
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
