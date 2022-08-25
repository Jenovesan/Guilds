package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;

public class GuildCreationEvent extends GuildsEvent {
    private final Guild guild;

    public Guild getGuild() {
        return guild;
    }

    public GuildCreationEvent(Guild guild) {
        this.guild = guild;
    }
}
