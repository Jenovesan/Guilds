package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.Guild;

public class GuildCreatedEvent extends GuildsEvent {
    private final Guild guild;

    public Guild getGuild() {
        return guild;
    }

    public GuildCreatedEvent(Guild guild) {
        this.guild = guild;
    }
}
