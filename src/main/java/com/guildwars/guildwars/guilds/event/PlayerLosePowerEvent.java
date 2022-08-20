package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLosePowerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;
    private final gPlayer killer;

    public gPlayer getKiller() {
        return killer;
    }

    public gPlayer getPlayer() {
        return player;
    }

    public PlayerLosePowerEvent(gPlayer player, gPlayer killer) {
        this.player = player;
        this.killer = killer;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
