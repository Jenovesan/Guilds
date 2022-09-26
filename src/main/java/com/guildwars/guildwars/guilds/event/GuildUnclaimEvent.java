package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.GuildChunk;

public class GuildUnclaimEvent extends GuildsEvent {

    private final Guild guild;

    private final GuildChunk chunk;

    public Guild getGuild() {
        return guild;
    }

    public GuildChunk getChunk() {
        return chunk;
    }

    // Not called on guild disband
    public GuildUnclaimEvent(Guild guild, GuildChunk chunk) {
        this.guild = guild;
        this.chunk = chunk;
    }

}
