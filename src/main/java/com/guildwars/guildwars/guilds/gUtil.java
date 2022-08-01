package com.guildwars.guildwars.guilds;

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
}
