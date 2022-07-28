package com.guildwars.guildwars.guilds;

import java.util.UUID;

public class gUtil {
    public static Guild getPlayerGuild(UUID playerUUID) {
        for (Guild guild : GuildsManager.getGuilds()) {
            if (guild.players.containsKey(playerUUID)) {
                return guild;
            }
        }
        return null;
    }
}
