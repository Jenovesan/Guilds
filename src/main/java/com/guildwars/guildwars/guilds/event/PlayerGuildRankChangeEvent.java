package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGuildRankChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;
    private final GuildRank newRank;

    public gPlayer getPlayer() {
        return player;
    }

    public GuildRank getNewRank() {
        return newRank;
    }

    public PlayerGuildRankChangeEvent(gPlayer player, GuildRank newRank) {
        this.player = player;
        this.newRank = newRank;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
