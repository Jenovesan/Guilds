package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.GuildRank;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildPermissionChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

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


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
