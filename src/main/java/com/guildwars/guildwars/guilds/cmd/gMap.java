package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class gMap extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.map.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.map.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    static final int gMapSize = Config.get().getInt("g map radius (chunks)");

    @Override
    public void perform(gPlayer player, String[] args) {
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

        for (int z = playerChunkZ + gMapSize; z >= playerChunkZ - gMapSize; z--) {
            mapMsg = mapMsg.concat("\n" + ChatColor.RESET);

            if (z == playerChunkZ) { // Center of the map (z-axis)
                mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.west") + wildernessClaimPrefix + "|");
            } else {
                mapMsg = mapMsg.concat("  ");
            }

            for (int x = playerChunkX + gMapSize; x >= playerChunkX - gMapSize; x--) {
                // Add player symbol at center of map
                if (x == playerChunkX && z == playerChunkZ) {
                    mapMsg = mapMsg.concat(Messages.getMsg("commands.map.map construction.player symbol"));
                    guildsOnMap.put(playerGuild, playerClaimPrefix); // So player's claim shows up on Guild's list
                    continue;
                }


                Guild guildAtChunk = Board.getBoard()[Board.getChunkCord(x)][Board.getChunkCord(z)].getGuild();

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
        mapMsg = mapMsg.concat("\n" + constructFooter(guildsOnMap));

        player.sendMessage(mapMsg);
    }

    private static String constructFooter(HashMap<Guild, String> guildsOnMap) {
        // No Guilds on map
        if (guildsOnMap.isEmpty()) {
            return Messages.getMsg("commands.map.map construction.footer without guilds");
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
            return Messages.getMsg("commands.map.map construction.footer with guilds", null, null, guildsList);
        }
    }
}
