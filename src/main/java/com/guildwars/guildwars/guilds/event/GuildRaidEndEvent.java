package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildRaidEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Guild raidableGuild;
    private final Guild raidingGuild;

    public Guild getRaidableGuild() {
        return raidableGuild;
    }

    public Guild getRaidingGuild() {
        return raidingGuild;
    }

    public GuildRaidEndEvent(Guild raidableGuild, Guild raidingGuild) {
        this.raidableGuild = raidableGuild;
        this.raidingGuild = raidingGuild;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
