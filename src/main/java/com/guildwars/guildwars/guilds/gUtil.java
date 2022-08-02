package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.util;

import java.util.UUID;

public class gUtil {

    public static Guild getOfflinePlayerGuild(UUID playerUUID) {
        for (Guild guild : Guilds.getGuilds()) {
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
            gPlayer.sendFailMsg(Messages.getMsg("commands.guild rank too low").replace("<rank>", util.formatEnum(permMinRank)));
            return false;
        }
    }
}
