package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;

public class GuildDisbandEvent extends GuildsEvent {
    private final Guild guild;

    public Guild getGuild() {
        return guild;
    }

    public GuildDisbandEvent(Guild guild) {
        this.guild = guild;
    }
}
