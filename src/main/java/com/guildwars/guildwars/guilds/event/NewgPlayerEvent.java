package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewgPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;

    public gPlayer getPlayer() {
        return player;
    }

    public NewgPlayerEvent(gPlayer gPlayer) {
        this.player = gPlayer;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
