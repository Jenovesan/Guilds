package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.entity.BoardCord;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.guilds.files.BoardData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;

public class Board extends Coll<GuildChunk>{

    public static Board i = new Board();
    public static Board get() {
        return i;
    }

    private final int worldClaimRadius = Config.get(Plugin.GUILDS).getInt("world claim radius (chunks)");

    public int getRadius() {
        return worldClaimRadius;
    }

    private final GuildChunk[][] board = new GuildChunk[worldClaimRadius * 2][worldClaimRadius * 2];

    public GuildChunk[][] getBoard() {
        return board;
    }

    public void load() {
        List<FileConfiguration> boardData = BoardData.get().getAllData();

        // Fill board with guild claims
        for (FileConfiguration boardFile : boardData) {
            // boardLocation
            String[] boardLocationRaw = boardFile.getString("boardLocation").split(",");
            BoardCord boardCord = new BoardCord(Integer.parseInt(boardLocationRaw[0]), Integer.parseInt(boardLocationRaw[1]), false);

            // guildId
            String guildId = boardFile.getString("guildId");

            // Create GuildChunk
            GuildChunk newChunk = new GuildChunk(Guilds.get().getById(guildId), boardCord);

            // Add GuildChunk
            getAll().add(newChunk);

            // Add to board
            getBoard()[newChunk.getBoardLocation().getX()][newChunk.getBoardLocation().getZ()] = newChunk;
        }

        // Fill remaining board spaces with empty GuildChunks
        for (int x = 0; x < getBoard().length; x++) {
            for (int z = 0; z < getBoard().length; z++) {
                if (getBoard()[x][z] != null) continue;
                getBoard()[x][z] = new GuildChunk(null, new BoardCord(x, z, false));
            }
        }
    }

    public GuildChunk getChunkAt(Chunk chunk) {
        return getGuildChunkAt(new BoardCord(chunk));
    }

    public GuildChunk getGuildChunkAt(Location location) {
        return getGuildChunkAt(new BoardCord(location));
    }

    public GuildChunk getGuildChunkAt(int x, int z) {
        return getGuildChunkAt(new BoardCord(x, z, true));
    }

    private GuildChunk getGuildChunkAt(BoardCord boardCord) {
        if (boardCord.isOutOfBounds()) return null;
        return getBoard()[boardCord.getX()][boardCord.getZ()];
    }

    public Guild getGuildAt(Location location) {
        return getGuildChunkAt(location).getGuild();
    }

    public HashSet<GuildChunk> getGuildClaims(Guild guild) {
        HashSet<GuildChunk> guildClaims = new HashSet<>();
        for (GuildChunk chunk : getAll()) {
            if (chunk.getGuild() == guild) {
                guildClaims.add(chunk);
            }
        }
        return guildClaims;
    }
}