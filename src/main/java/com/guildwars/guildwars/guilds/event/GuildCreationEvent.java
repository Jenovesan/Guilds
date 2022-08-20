package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildCreationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Guild guild;

    public Guild getGuild() {
        return guild;
    }

    public GuildCreationEvent(Guild guild) {
        this.guild = guild;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
