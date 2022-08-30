package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;

public class gUtil {

    public static boolean checkPermission(gPlayer player, GuildPermission permission, boolean sendReturnMsg) {
        GuildRank guildRank = player.getGuildRank();
        GuildRank permMinRank = player.getGuild().getPermissions().get(permission);
        if (guildRank.level >= permMinRank.level) {
            return true;
        } else { // Guild rank too low
            if (sendReturnMsg) {
                player.sendFailMsg(Messages.getMsg("commands.guild rank too low", permMinRank));
            }
            return false;
        }
    }

    public static boolean guildNameLegal(gPlayer player, String guildName) {
        // Check if guild name is too long
        int guildNameCharacterLimit = Config.get().getInt("max characters in guild name");
        if (guildName.toCharArray().length > guildNameCharacterLimit) {
            player.sendFailMsg(Messages.getMsg("guild naming.name too long", guildName));
            return false;
        }

        // Check if the guild name exists already
        for (Guild guild : Guilds.get().getAll()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                player.sendFailMsg(Messages.getMsg("guild naming.name exists", guildName));
                return false;
            }
        }

        // Check if guild name contains any non-listed characters
        List<Character> legalGuildNameCharacters = Config.get().getCharacterList("valid guild name characters");
        for (Character character : guildName.toCharArray()) {
            if (!legalGuildNameCharacters.contains(character)) {
                player.sendFailMsg(Messages.getMsg("guild naming.name contains not legal character", character));
                return false;
            }
        }

        // Check if the name is a blacklisted guild name
        List<String> blackListedGuildNames = Config.get().getStringList("blacklisted guild names");
        if (blackListedGuildNames.contains(guildName)) {
            player.sendFailMsg(Messages.getMsg("guild naming.name blacklisted"));
            return false;
        }

        // Guild name passes all checks.
        // Guild name is deemed legal.
        return true;
    }

    public static boolean isInMainWorld(gPlayer player) {
        return player.getPlayer().getWorld().getName().equals(Config.get().getString("world name"));
    }

    public static String getNewUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getGuildToName(gPlayer player, Guild guild) {
        Guild playerGuild = player.getGuild();
        if (playerGuild == guild) {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("own guild name prefix")) + guild.getName();
        }
        else if (playerGuild.isEnemied(guild)) {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("enemy guild name prefix")) + guild.getName();
        }
        else {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("other guild name prefix")) + guild.getName();
        }
    }

    public static String getGuildToDesc(gPlayer player, Guild guild) {
        Guild playerGuild = player.getGuild();
        if (playerGuild == guild) {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("own guild desc prefix")) + guild.getName();
        }
        else if (playerGuild.isEnemied(guild)) {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("enemy guild desc prefix")) + guild.getName();
        }
        else {
            return ChatColor.translateAlternateColorCodes('&', Config.get().getString("other guild desc prefix")) + guild.getName();
        }
    }
}
