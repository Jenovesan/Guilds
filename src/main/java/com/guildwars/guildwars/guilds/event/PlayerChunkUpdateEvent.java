package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.*;
import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;

public class PlayerChunkUpdateEvent extends GuildsEvent implements Cancellable {

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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
