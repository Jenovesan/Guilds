package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class Board {

    private final static int worldClaimRadius = Config.get().getInt("world claim radius (chunks)");

    private static GuildChunk[][] board = new GuildChunk[worldClaimRadius * 2][worldClaimRadius * 2];

    public static GuildChunk[][] getBoard() {
        return board;
    }

    public static void fillBoard() {
        // Fill board with Guilds' claims
        for (Guild guild : Guilds.getAllGuilds()) {
            for (int[] claimLocation : guild.getClaimLocations()) {
                getBoard()[claimLocation[0]][claimLocation[1]] = new GuildChunk(guild, claimLocation);
            }
        }

        // Fill remaining board spaces with empty GuildChunks
        for (int x = 0; x < board.length; x++) {
            for (int z = 0; z < board.length; z++) {
                if (board[x][z] != null) {
                    continue;
                }
                getBoard()[x][z] = new GuildChunk(null, new int[]{x, z});
            }
        }
    }

    public static int getChunkCord(int chunkCord) {
        if (chunkCord < 0) {
            return worldClaimRadius + Math.abs(chunkCord);
        }
        return chunkCord;
    }

    public static int[] getChunkBoardLocation(Chunk chunk) {
        return new int[] {getChunkCord(chunk.getX()), getChunkCord(chunk.getZ())};
    }

    public static Guild getGuildAt(Location location) {
        int[] chunkBoardLocation = getChunkBoardLocation(location.getChunk());
        return getBoard()[chunkBoardLocation[0]][chunkBoardLocation[1]].getGuild();
    }

    public static int getGuildIdAt(Location location) {
        int[] chunkBoardLocation = getChunkBoardLocation(location.getChunk());
        return getBoard()[chunkBoardLocation[0]][chunkBoardLocation[1]].getGuildId();
    }

    public static GuildChunk getGuildChunkAt(Location location) {
        int chunkX = getChunkCord(location.getChunk().getX());
        int chunkZ = getChunkCord(location.getChunk().getZ());
        return getBoard()[chunkX][chunkZ];
    }
}
