package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeNameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;
    private final String name;

    public gPlayer getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public PlayerChangeNameEvent(gPlayer player, String name) {
        this.player = player;
        this.name = name;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
