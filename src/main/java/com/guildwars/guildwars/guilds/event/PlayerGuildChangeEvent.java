package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGuildChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Guild newGuild;
    private final Reason reason;

    public Player getPlayer() {
        return player;
    }

    public Guild getNewGuild() {
        return newGuild;
    }

    public Reason getReason() {
        return reason;
    }

    public PlayerGuildChangeEvent(Player player, Guild newGuild, Reason reason) {
        this.player = player;
        this.newGuild = newGuild;
        this.reason = reason;
    }

    public enum Reason {
        CREATION,
        DISBAND,
        JOIN,
        LEAVE,
        KICKED
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
