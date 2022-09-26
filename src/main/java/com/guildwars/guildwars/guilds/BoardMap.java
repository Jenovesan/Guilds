package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.entity.GuildChunk;
import com.guildwars.guildwars.utils.util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class BoardMap implements Runnable {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    private static final int mapSize = Config.get(Plugin.GUILDS).getInt("g map radius (chunks)");

    private static final String MAP_PATH = "commands.map.";
    private static final String MAP_CONSTRUCTION_PATH = MAP_PATH + "map construction.";

    private static final String WILDERNESS_CLAIM_PREFIX = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "wilderness claim prefix");
    private static final String PLAYER_CLAIM_PREFIX = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "player guild claim prefix");
    private static final String OUTLANDS_PREFIX = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "outlands prefix");

    private static final String CLAIM_SYMBOL = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "claim symbol");

    private static final String TITLE = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "header");
    private static final String FOOTER_WITHOUT_GUILDS = Messages.get(Plugin.GUILDS).get(MAP_CONSTRUCTION_PATH + "footer without guilds");

    private static final String[] GUILD_COLORS = Messages.get(Plugin.GUILDS).getArr(MAP_CONSTRUCTION_PATH + "guild colors");

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final GPlayer player;
    private final Guild playerGuild;
    private final int playerChunkX;
    private final int playerChunkZ;

    HashMap<Guild, String> guildsOnMap = new HashMap<>();

    public BoardMap(GPlayer player) {
        this.player = player;
        this.playerGuild = player.getGuild();
        this.playerChunkX = player.getPlayer().getLocation().getChunk().getX();
        this.playerChunkZ = player.getPlayer().getLocation().getChunk().getZ();
    }

    public void sendMap() {
        Bukkit.getScheduler().runTaskAsynchronously(GuildWars.getInstance(), this);
    }

    @Override
    public void run() {
        String map = getTitle().concat(getMap()).concat(getFooter());
        player.sendMessage(map);
    }

    private String getTitle() {
        return TITLE;
    }


    private String getMap() {
        String map = "";
        for (int z = -mapSize; z <= mapSize; z++) {
            map = map.concat("\n" + ChatColor.RESET);

            if (z == 0) { // Center of the map (z-axis)
                map = map.concat(Messages.get(Plugin.GUILDS).get("commands.map.map construction.west"));
            } else {
                map = map.concat("  ");
            }

            for (int x = -mapSize; x <= mapSize; x++) {

                GuildChunk chunk = Board.get().getGuildChunkAt(playerChunkX + x, playerChunkZ + z);
                // Add player symbol at center of map
                if (x == 0 && z == 0) {
                    map = map.concat(Messages.get(Plugin.GUILDS).get("commands.map.map construction.player symbol"));
                    if (chunk != null && player.isInGuild() && chunk.getGuild() == playerGuild) {
                        guildsOnMap.put(playerGuild, PLAYER_CLAIM_PREFIX); // So player's claim shows up on Guild's list
                    }
                    continue;
                }

                // Is border
                if (chunk == null) {
                    map = map.concat(OUTLANDS_PREFIX + CLAIM_SYMBOL);
                    continue;
                }

                Guild guildAtChunk = chunk.getGuild();

                // Wilderness claim
                if (guildAtChunk == null) {
                    map = map.concat(WILDERNESS_CLAIM_PREFIX + CLAIM_SYMBOL);
                    continue;
                }

                // Guild claim
                String guildClaimPrefix = guildsOnMap.get(guildAtChunk);
                if (guildClaimPrefix != null) { // Guild has already been registered on map
                    map = map.concat(guildClaimPrefix + CLAIM_SYMBOL);
                } else { // First time guild is being added to map
                    // Get new guild claim prefix
                    String newGuildPrefix;
                    // Player's guild
                    if (playerGuild != null && guildAtChunk == playerGuild) {
                        newGuildPrefix = PLAYER_CLAIM_PREFIX;
                    } else {
                        if (guildsOnMap.size() == GUILD_COLORS.length) {
                            // More guilds on map than guildColors.
                            // Generate random color.
                            newGuildPrefix = util.getRandomColor().toString();
                        } else {
                            newGuildPrefix = GUILD_COLORS[guildsOnMap.size()];
                        }
                    }
                    // Add guild to guildsOnMap
                    guildsOnMap.put(guildAtChunk, newGuildPrefix);
                    // Add claim on mapMsg
                    map = map.concat(newGuildPrefix + CLAIM_SYMBOL);
                }
            }
            if (z == 0) { // Center of the map (z-axis)
                map = map.concat(Messages.get(Plugin.GUILDS).get("commands.map.map construction.east"));
            }
        }
        return map;
    }

    private String getFooter() {
        if (guildsOnMap.isEmpty()) return "\n" + FOOTER_WITHOUT_GUILDS;

        // Add legend of guild to footer/map

        String guildsList = "";

        int numberOfGuilds = guildsOnMap.size();
        int i = 0;

        for (Map.Entry<Guild, String> entry : guildsOnMap.entrySet()) {
            // Vars
            i++;
            String guildName = entry.getKey().getName();
            String guildPrefix = entry.getValue();

            // Add guild name with its respective color
            guildsList = guildsList.concat(guildPrefix + guildName);

            // Add comma/delimiter after guild name
            if (i != numberOfGuilds) {
                guildsList = guildsList.concat(Messages.get(Plugin.GUILDS).get("commands.map.map construction.guilds list delimiter"));
            }
        }

        return "\n" + Messages.get(Plugin.GUILDS).get("commands.map.map construction.footer with guilds", guildsList);
    }
}
