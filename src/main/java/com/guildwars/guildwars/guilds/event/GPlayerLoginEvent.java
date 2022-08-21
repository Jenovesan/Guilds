package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GPlayerLoginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final com.guildwars.guildwars.guilds.gPlayer gPlayer;
    private final boolean newPlayer;

    public Player getPlayer() {
        return player;
    }

    public gPlayer getGPlayer() {
        return gPlayer;
    }

    public boolean isNewPlayer() {
        return newPlayer;
    }

    public GPlayerLoginEvent(Player player, gPlayer gPlayer, boolean newPlayer) {
        this.player = player;
        this.gPlayer = gPlayer;
        this.newPlayer = newPlayer;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
