package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
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
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.guild rank too low", permMinRank));
            }
            return false;
        }
    }

    public static boolean guildNameLegal(gPlayer player, String guildName) {
        // Check if guild name is too long
        int guildNameCharacterLimit = Config.get(Plugin.GUILDS).getInt("max characters in guild name");
        if (guildName.toCharArray().length > guildNameCharacterLimit) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("guild naming.name too long", guildName));
            return false;
        }

        // Check if the guild name exists already
        for (Guild guild : Guilds.get().getAll()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("guild naming.name exists", guildName));
                return false;
            }
        }

        // Check if guild name contains any non-listed characters
        List<Character> legalGuildNameCharacters = Config.get(Plugin.GUILDS).getCharacterList("valid guild name characters");
        for (Character character : guildName.toCharArray()) {
            if (!legalGuildNameCharacters.contains(character)) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("guild naming.name contains not legal character", character));
                return false;
            }
        }

        // Check if the name is a blacklisted guild name
        List<String> blackListedGuildNames = Config.get(Plugin.GUILDS).getStringList("blacklisted guild names");
        if (blackListedGuildNames.contains(guildName)) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("guild naming.name blacklisted"));
            return false;
        }

        // Guild name passes all checks.
        // Guild name is deemed legal.
        return true;
    }

    public static boolean isInMainWorld(gPlayer player) {
        return player.getPlayer().getWorld().getName().equals(Config.get(Plugin.GUILDS).getString("world name"));
    }

    public static String getNewUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getGuildToName(gPlayer player, Guild guild) {
        Guild playerGuild = player.getGuild();
        if (playerGuild == guild) {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("own guild name prefix")) + guild.getName();
        }
        else if (playerGuild.isEnemied(guild)) {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("enemy guild name prefix")) + guild.getName();
        }
        else {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("other guild name prefix")) + guild.getName();
        }
    }

    public static String getGuildToDesc(gPlayer player, Guild guild) {
        Guild playerGuild = player.getGuild();
        if (playerGuild == guild) {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("own guild desc prefix")) + guild.getName();
        }
        else if (playerGuild.isEnemied(guild)) {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("enemy guild desc prefix")) + guild.getName();
        }
        else {
            return ChatColor.translateAlternateColorCodes('&', Config.get(Plugin.GUILDS).getString("other guild desc prefix")) + guild.getName();
        }
    }
}
