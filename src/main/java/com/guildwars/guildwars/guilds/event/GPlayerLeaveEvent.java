package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GPlayerLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer gPlayer;

    public gPlayer getGPlayer() {
        return gPlayer;
    }

    public GPlayerLeaveEvent(gPlayer gPlayer) {
        this.gPlayer = gPlayer;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
