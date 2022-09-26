package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.GuildRank;

public class GuildPermissionChangeEvent extends GuildsEvent {

    private final Guild guild;
    private final GuildPermission permission;
    private final GuildRank newGuildRank;
    private final GuildRank oldGuildRank;

    public Guild getGuild() {
        return guild;
    }

    public GuildPermission getPermission() {
        return permission;
    }

    public GuildRank getNewGuildRank() {
        return newGuildRank;
    }

    public GuildRank getOldGuildRank() {
        return oldGuildRank;
    }

    public GuildPermissionChangeEvent(Guild guild, GuildPermission permission, GuildRank oldGuildRank, GuildRank newGuildrank) {
        this.guild = guild;
        this.permission = permission;
        this.oldGuildRank = oldGuildRank;
        this.newGuildRank = newGuildrank;
    }
}
