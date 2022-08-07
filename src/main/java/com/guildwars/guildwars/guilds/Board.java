package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;

public class Board {

    private final static int worldClaimRadius = Config.get().getInt("world claim size (chunks)");

    private static GuildChunk[][] board = new GuildChunk[worldClaimRadius * 2][worldClaimRadius * 2];

    private static GuildChunk[][] getBoard() {
        return board;
    }

    public static void fillBoard() {
        for (int x = 0; x < board.length; x++) {
            for (int z = 0; z < board.length; z++) {
                getBoard()[x][z] = new GuildChunk(null);
            }
        }
    }

    private static int getChunkCord(int chunkCord) {
        if (chunkCord < 0) {
            return worldClaimRadius + Math.abs(chunkCord);
        }
        return chunkCord;
    }

    public static Guild getGuildAt(int chunkX, int chunkZ) {
        chunkX = getChunkCord(chunkX);
        chunkZ = getChunkCord(chunkZ);
        return getBoard()[chunkX][chunkZ].getGuild();
    }
}
