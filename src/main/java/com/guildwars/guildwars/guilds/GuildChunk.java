package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.entity.BoardCord;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.files.BoardData;
import org.bukkit.Chunk;

public class GuildChunk {

    private Guild guild;
    private final BoardCord boardCord;
    private final Chunk chunk;

    public void claim(Guild hostGuild) {
        guild = hostGuild;
        changed();
    }

    public Guild getGuild() {
        return guild;
    }

    public BoardCord getBoardLocation() {
        return boardCord;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public GuildChunk(Guild guild, BoardCord boardCord) {
        this.guild = guild;
        this.boardCord = boardCord;
        this.chunk = boardCord.getBukkitChunk();
    }

    public boolean isClaimable() {
        return this.isWilderness();
    }

    public boolean hasConnectingClaim(Guild guild) {
        int x = chunk.getX();
        int z = chunk.getZ();
        // North chunk
        GuildChunk chunkNorth = Board.get().getGuildChunkAt(x, z + 1);
        if (chunkNorth != null && chunkNorth.getGuild() == guild) return true;

        // South chunk
        GuildChunk chunkSouth = Board.get().getGuildChunkAt(x, z - 1);
        if (chunkSouth != null && chunkSouth.getGuild() == guild) return true;

        // East chunk
        GuildChunk chunkEast = Board.get().getGuildChunkAt(x + 1, z);
        if (chunkEast != null && chunkEast.getGuild() == guild) return true;

        // West chunk
        GuildChunk chunkWest = Board.get().getGuildChunkAt(x - 1, z);
        return chunkWest != null && chunkWest.getGuild() == guild;
    }

    public boolean isWilderness() {
        return guild == null;
    }

    public void setWilderness() {
        guild = null;
        changed();
    }

    private void changed() {
        // If the chunk is no longer claimed, remove its data
        if (isWilderness()) Board.get().remove(this);
        // If the chunk is claimed, save its data and add it to  board
        else {
            // Add to Board
            Board.get().add(this);

            // Save data
            BoardData.get().save(this);
        }
    }
}
