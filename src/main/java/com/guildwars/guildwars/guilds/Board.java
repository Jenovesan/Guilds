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
        for (Guild guild : Guilds.get().getAll()) {
            for (int[] claimLocation : guild.getClaimLocations()) {
                getBoard()[claimLocation[0]][claimLocation[1]] = new GuildChunk(guild, claimLocation);
            }
        }

        // Fill remaining board spaces with empty GuildChunks
        for (int x = 0; x < getBoard().length; x++) {
            for (int z = 0; z < getBoard().length; z++) {
                if (getBoard()[x][z] != null) {
                    continue;
                }
                getBoard()[x][z] = new GuildChunk(null, new int[]{x, z});
            }
        }
    }

    public static int getChunkCord(int chunkCord) {
        if (chunkCord > 0) {
            return worldClaimRadius + chunkCord;
        } else {
            return worldClaimRadius - (chunkCord * -1);
        }
    }

    public static GuildChunk getChunk(Chunk chunk) {
        return getBoard()[getChunkCord(chunk.getX())][getChunkCord(chunk.getZ())];
    }

    public static GuildChunk getGuildChunkAt(Location location) {
        return getGuildChunkAt(location.getChunk().getX(), location.getChunk().getZ());
    }

    public static GuildChunk getGuildChunkAt(int xRaw, int zRaw) {
        int x = getChunkCord(xRaw);
        int z = getChunkCord(zRaw);
        if (x < 0 || x >= worldClaimRadius * 2 || z < 0 || z >= worldClaimRadius * 2) {
            return null;
        }
        return getBoard()[x][z];
    }

    public static GuildChunk getGuildChunkAt(int[] boardLocation) {
        int x = boardLocation[0];
        int z = boardLocation[1];
        if (x < 0 || x >= worldClaimRadius * 2 || z < 0 || z >= worldClaimRadius * 2) {
            return null;
        }
        return getBoard()[x][z];
    }

    public static GuildChunk[] getNearbyChunks(Location center, int radius) {
        // Uses a reverse-spiral matrix algorithm so claims will be connected while claiming in a radius

        // Get starting position
        int x = center.getChunk().getX();
        int z = center.getChunk().getZ();

        // Create variables
        int diameter = (radius * 2) + 1;
        int size = diameter * diameter;
        GuildChunk[] nearbyChunks = new GuildChunk[size];

        // Create variables for reverse-spiral
        int len = 0;
        int d = 0;
        int[] directions = new int[]{0, 1, 0, -1, 0};

        // Reverse-spiral
        for (int i = 0; i < size;) {
            if (d == 0 || d == 2) {
                len++;
            }
            for (int k = 0; k < len; k++) {
                nearbyChunks[i++] = getGuildChunkAt(x, z);
                x += directions[d];
                z += directions[d + 1];
            }

            d = ++d % 4;
        }
        return nearbyChunks;
    }


}