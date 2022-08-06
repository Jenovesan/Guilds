package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class gUtil {

    public static Guild getOfflinePlayerGuild(UUID playerUUID) {
        for (Guild guild : Guilds.getAllGuilds()) {
            if (guild.getPlayers().containsKey(playerUUID)) {
                return guild;
            }
        }
        return null;
    }

    public static boolean checkPermission(gPlayer gPlayer, GuildPermission permission) {
        GuildRank guildRank = gPlayer.getGuildRank();
        GuildRank permMinRank = gPlayer.getGuild().getPermissions().get(permission);
        if (guildRank.level >= permMinRank.level) {
            return true;
        } else { // Guild rank too low
            gPlayer.sendFailMsg(Messages.getMsg("commands.guild rank too low", gPlayer.getPlayer(), null, null, gPlayer.getGuild(), null, guildRank, permMinRank));
            return false;
        }
    }

    public static boolean guildNameLegal(Player player, String guildName) {
        // Check if guild name is too long
        int guildNameCharacterLimit = Config.get().getInt("max characters in guild name");
        if (guildName.toCharArray().length > guildNameCharacterLimit) {
            pUtil.sendFailMsg(player, Messages.getMsg("guild naming.name too long", player, null, new String[] {guildName}, null, null, null, null));
            return false;
        }

        // Check if the guild name exists already
        for (Guild guild : Guilds.getAllGuilds()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                pUtil.sendFailMsg(player, Messages.getMsg("guild naming.name exists", player, null, new String[] {guildName}, null, null, null, null));
                return false;
            }
        }

        // Check if guild name contains any non-listed characters
        List<Character> legalGuildNameCharacters = Config.get().getCharacterList("valid guild name characters");
        for (Character character : guildName.toCharArray()) {
            if (!legalGuildNameCharacters.contains(character)) {
                pUtil.sendFailMsg(player, Messages.getMsg("guild naming.name contains not legal character", player, null, new String[] {guildName}, null, null, null, null));
                return false;
            }
        }

        // Check if the name is a blacklisted guild name
        List<String> blackListedGuildNames = Config.get().getStringList("blacklisted guild names");
        if (blackListedGuildNames.contains(guildName)) {
            pUtil.sendFailMsg(player, Messages.getMsg("guild naming.name blacklisted", player, null, new String[] {guildName}, null, null, null, null));
            return false;
        }

        // Guild name passes all checks.
        // Guild name is deemed legal.
        return true;
    }
}
