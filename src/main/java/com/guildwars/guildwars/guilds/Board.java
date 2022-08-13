package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.util;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

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
            return chunkCord * -1;
        }
    }

    public static int[] getChunkBoardLocation(Chunk chunk) {
        return new int[] {getChunkCord(chunk.getX()), getChunkCord(chunk.getZ())};
    }

    public static GuildChunk getChunk(Chunk chunk) {
        return getBoard()[getChunkCord(chunk.getX())][getChunkCord(chunk.getZ())];
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

    static final int gMapSize = Config.get().getInt("g map radius (chunks)");

    public static String getMap(gPlayer player) {
        Guild playerGuild = player.getGuild();

        int playerChunkX = player.getPlayer().getLocation().getChunk().getX();
        int playerChunkZ = player.getPlayer().getLocation().getChunk().getZ();

        // Create map
        HashMap<Guild, String> guildsOnMap = new HashMap<>();

        String mapMsg = Messages.getMsg("commands.map.map construction.header");
        String wildernessClaimPrefix = Messages.getMsg("commands.map.map construction.wilderness claim prefix");
        String playerClaimPrefix = Messages.getMsg("commands.map.map construction.player guild claim prefix");
        String claimSymbol = Messages.getMsg("commands.map.map construction.claim symbol");
        String[] guildColors = Messages.getStringArray("commands.map.map construction.guild colors");

        for (int z = playerChunkZ - gMapSize; z <= playerChunkZ + gMapSize; z++) {
            mapMsg = mapMsg.concat("\n" + ChatColor.RESET);

            if (z == playerChunkZ) { // Center of the map (z-axis)
                mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.west") + wildernessClaimPrefix + "|");
            } else {
                mapMsg = mapMsg.concat("  ");
            }

            for (int x = playerChunkX - gMapSize; x <= playerChunkX + gMapSize; x++) {

                Guild guildAtChunk = Board.getBoard()[Board.getChunkCord(x)][Board.getChunkCord(z)].getGuild();

                // Add player symbol at center of map
                if (x == playerChunkX && z == playerChunkZ) {
                    mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.player symbol"));
                    if (guildAtChunk == playerGuild) {
                        guildsOnMap.put(playerGuild, playerClaimPrefix); // So player's claim shows up on Guild's list
                    }
                    continue;
                }

                // Wilderness claim
                if (guildAtChunk == null) {
                    mapMsg = mapMsg.concat(wildernessClaimPrefix + claimSymbol);
                    continue;
                }

                // Guild claim
                String guildClaimPrefix = guildsOnMap.get(guildAtChunk);
                if (guildClaimPrefix != null) { // Guild has already been registered on map
                    mapMsg = mapMsg.concat(guildClaimPrefix + claimSymbol);
                } else { // First time guild is being added to map
                    // Get new guild claim prefix
                    String newGuildPrefix;
                    // Player's guild
                    if (playerGuild != null && guildAtChunk == playerGuild) {
                        newGuildPrefix = playerClaimPrefix;
                    } else {
                        if (guildsOnMap.size() == guildColors.length) {
                            // More guilds on map than guildColors.
                            // Generate random color.
                            newGuildPrefix = util.getRandomColor().toString();
                        } else {
                            newGuildPrefix = guildColors[guildsOnMap.size()];
                        }
                    }
                    // Add guild to guildsOnMap
                    guildsOnMap.put(guildAtChunk, newGuildPrefix);
                    // Add claim on mapMsg
                    mapMsg = mapMsg.concat(newGuildPrefix + claimSymbol);
                }
            }
            if (z == playerChunkX) { // Center of the map (z-axis)
                mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.east"));
            }
        }
        mapMsg = mapMsg.concat("\n");

        if (guildsOnMap.isEmpty()) {
            mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.footer without guilds"));
        }
        // Guilds on map
        else {
            String guildsList = "";
            int numberOfGuilds = guildsOnMap.size();
            int i = 0;
            for (Map.Entry<Guild, String> entry : guildsOnMap.entrySet()) {
                i++;
                String guildName = entry.getKey().getName();
                String guildPrefix = entry.getValue();
                guildsList = guildsList.concat(guildPrefix + guildName);
                if (i != numberOfGuilds) {
                    guildsList = guildsList.concat(Messages.getMsg("commands.map.map construction.guilds list delimiter"));
                }
            }
            mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.footer with guilds", null, null, guildsList));
        }

        return mapMsg;

    }
}
