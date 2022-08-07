package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class gMap extends gCommand{
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    static final int gMapSize = Config.get().getInt("g map radius (chunks)");

    @Override
    public void perform(gPlayer gPlayer, String[] args) {
        Guild playerGuildId = gPlayer.getGuild();
        Player player = gPlayer.getPlayer();
        Chunk chunk = player.getLocation().getChunk();
        int playerChunkX = chunk.getX();
        int playerChunkZ = chunk.getZ();

        // Create map
        HashMap<Guild, String> guildsOnMap = new HashMap<>();

        String mapMsg = Messages.getMsg("commands.map.map construction.header");
        String wildernessClaimPrefix = Messages.getMsg("commands.map.map construction.wilderness claim prefix");
        String playerClaimPrefix = Messages.getMsg("commands.map.map construction.player guild claim prefix");
        String claimSymbol = Messages.getMsg("commands.map.map construction.claim symbol");
        String[] guildColors = Messages.getStringArray("commands.map.map construction.guild colors");

        for (int z = -gMapSize; z <= gMapSize; z++) {
            mapMsg = mapMsg.concat("\n" + ChatColor.RESET);

            if (z == 0) { // Center of the map (z-axis)
                mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.west") + wildernessClaimPrefix + "|");
            } else {
                mapMsg = mapMsg.concat("  ");
            }

            for (int x = -gMapSize; x <= gMapSize; x++) {
                // Add player symbol at center of map
                if (x == 0 && z == 0) {
                    mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.player symbol"));
                    continue;
                }

                int chunkX = playerChunkX + x;
                int chunkZ = playerChunkZ + z;

                Guild guildAtChunk = Board.getGuildAt(chunkX, chunkZ);

                // Wilderness claim
                if (guildAtChunk == null) {
                    mapMsg = mapMsg.concat(wildernessClaimPrefix + "■ ");
                    continue;
                }

                // Player guild's claim
                if (guildAtChunk == playerGuildId) {
                    mapMsg = mapMsg.concat(playerClaimPrefix + claimSymbol);
                    continue;
                }

                // Guild claim
                String guildClaimPrefix = guildsOnMap.get(guildAtChunk);
                if (guildClaimPrefix != null) { // Guild has already been registered on map
                    mapMsg = mapMsg.concat(guildClaimPrefix + claimSymbol);
                } else { // First time guild is being added to map
                    // Get new guild claim prefix
                    String newGuildPrefix;
                    if (guildsOnMap.size() == guildColors.length) {
                        // More guilds on map than guildColors.
                        // Generate random color.
                        newGuildPrefix = util.getRandomColor().toString();
                    } else {
                        newGuildPrefix = guildColors[guildsOnMap.size()];
                    }
                    // Add guild to guildsOnMap
                    guildsOnMap.put(guildAtChunk, newGuildPrefix);
                    // Add claim on mapMsg
                    mapMsg = mapMsg.concat(newGuildPrefix + claimSymbol);
                }
            }
            if (z == 0) { // Center of the map (z-axis)
                mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.east"));
            }
        }
        mapMsg = mapMsg.concat("\n" + Messages.getMsg("commands.map.map construction.footer"));

        // Construct footer
        for (Map.Entry<Guild, String> entry : guildsOnMap.entrySet()) {
            String guildName = entry.getKey().getName();
            String guildPrefix = entry.getValue();
            mapMsg = mapMsg.concat(guildPrefix + guildName + "  ");
        }

        player.sendMessage(mapMsg);
    }
}
