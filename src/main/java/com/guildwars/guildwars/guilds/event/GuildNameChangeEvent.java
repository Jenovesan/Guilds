package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildNameChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Guild guild;
    private final String oldName;
    private final String newName;

    public Guild getGuild() {
        return guild;
    }

    public String getNewName() {
        return newName;
    }

    public String getOldName() {
        return oldName;
    }

    public GuildNameChangeEvent(Guild guild, String oldName, String newName) {
        this.guild = guild;
        this.oldName = oldName;
        this.newName = newName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
