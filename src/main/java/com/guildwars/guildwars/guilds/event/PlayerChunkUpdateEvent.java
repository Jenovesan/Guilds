package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.*;
import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChunkUpdateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final gPlayer player;
    private final Chunk newChunk;
    private final GuildChunk newGuildChunk;

    public gPlayer getPlayer() {
        return player;
    }

    public Chunk getNewChunk() {
        return newChunk;
    }

    public GuildChunk getNewGuildChunk() {
        return newGuildChunk;
    }

    public PlayerChunkUpdateEvent(gPlayer player, Chunk newChunk) {
        this.player = player;
        this.newChunk = newChunk;
        this.newGuildChunk = Board.getChunk(newChunk);
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
