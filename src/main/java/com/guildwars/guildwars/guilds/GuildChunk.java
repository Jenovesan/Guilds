package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Messages;

import java.util.UUID;

public class GuildChunk {

    private Guild guild;
    private String guildId;
    private final int[] boardLocation;

    public void claim(Guild hostGuild) {
        this.guild = hostGuild;
        this.guildId = hostGuild != null ? hostGuild.getId() : null;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getGuildId() {
        return guildId;
    }

    public int[] getBoardLocation() {
        return this.boardLocation;
    }

    public GuildChunk(Guild hostGuild, int[] boardLocation) {
        this.guild = hostGuild;
        if (hostGuild != null) {
            this.guildId = hostGuild.getId();
        }
        this.boardLocation = boardLocation;
    }

    public boolean isClaimable() {
        return this.isWilderness();
    }

    public boolean hasConnectingClaim(Guild guild) {
        int x = boardLocation[0];
        int z = boardLocation[1];
        // North chunk
        GuildChunk chunkNorth = Board.getGuildChunkAt(new int[]{x, z+1});
        if (chunkNorth != null && chunkNorth.getGuild() == guild) {
            return true;
        }
        // South chunk
        GuildChunk chunkSouth = Board.getGuildChunkAt(new int[]{x, z-1});
        if (chunkSouth != null && chunkSouth.getGuild() == guild) {
            return true;
        }
        // East chunk
        GuildChunk chunkEast = Board.getGuildChunkAt(new int[]{x+1, z});
        if (chunkEast != null && chunkEast.getGuild() == guild) {
            return true;
        }
        // West chunk
        GuildChunk chunkWest = Board.getGuildChunkAt(new int[]{x-1, z});
        if (chunkWest != null && chunkWest.getGuild() == guild) {
            return true;
        }
        return false;
    }

    public boolean isWilderness() {
        return this.getGuildId() == null;
    }

    public void setWilderness() {
        this.guild = null;
        this.guildId = null;
    }

}
