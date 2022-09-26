package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;

public class PlayerChunkUpdateEvent extends GuildsEvent implements Cancellable {

    private final GPlayer player;
    private final Chunk newChunk;
    private final GuildChunk oldGuildChunk;
    private final GuildChunk newGuildChunk;

    public GPlayer getPlayer() {
        return player;
    }

    public Chunk getNewChunk() {
        return newChunk;
    }

    public GuildChunk getNewGuildChunk() {
        return newGuildChunk;
    }

    public GuildChunk getOldGuildChunk() {
        return oldGuildChunk;
    }

    public PlayerChunkUpdateEvent(GPlayer player, Chunk oldChunk, Chunk newChunk) {
        this.player = player;
        this.newChunk = newChunk;
        this.newGuildChunk = Board.get().getChunkAt(newChunk);
        this.oldGuildChunk = Board.get().getChunkAt(oldChunk);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
