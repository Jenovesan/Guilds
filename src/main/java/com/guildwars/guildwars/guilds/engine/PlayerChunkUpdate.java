package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerChunkUpdate implements Listener {

    private static HashMap<gPlayer, Location> lastlastLocations = new HashMap<>();

    private static HashMap<gPlayer, Location> getLastlastLocations() {
        return lastlastLocations;
    }

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<gPlayer, Location> playerLastChunks : getLastlastLocations().entrySet()) {
                    gPlayer player = playerLastChunks.getKey();
                    Player playerPlayer = player.getPlayer();
                    Location lastLocation = playerLastChunks.getValue();
                    Location newLocation = playerPlayer.getLocation();
                    Chunk lastChunk = lastLocation.getChunk();
                    Chunk newChunk = newLocation.getChunk();

                    if (lastChunk != newChunk) continue;

                    // Player moved into new chunk

                    // Call event
                    PlayerChunkUpdateEvent playerChunkUpdateEvent = new PlayerChunkUpdateEvent(player, newChunk);
                    Bukkit.getServer().getPluginManager().callEvent(playerChunkUpdateEvent);

                    if (playerChunkUpdateEvent.isCancelled()) {
                        playerPlayer.teleport(lastLocation);
                    } else {
                        // Update last location
                        lastlastLocations.replace(player, newLocation);
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), Config.get().getInt("player chunk update (ticks)"), Config.get().getInt("player chunk update (ticks)"));
    }

    @EventHandler
    public void addPlayerOnLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gPlayer gPlayer = gPlayersIndex.get().getByPlayer(player);
        getLastlastLocations().put(gPlayer, player.getLocation());
    }

    @EventHandler
    public void removePlayerOnLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        gPlayer gPlayer = gPlayersIndex.get().getByPlayer(player);
        getLastlastLocations().remove(gPlayer);
    }
}
