package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.HandlerList;

public class PlayerLosePowerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;

    public gPlayer getGlayer() {
        return player;
    }

    public PlayerLosePowerEvent(gPlayer player) {
        this.player = player;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
