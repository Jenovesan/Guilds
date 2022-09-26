package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.event.GPlayerQuitEvent;
import com.guildwars.guildwars.guilds.event.GPlayerLoginEvent;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.entity.GPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class PlayerChunkUpdate extends Engine {

    public PlayerChunkUpdate() {
        super(Config.get(Plugin.GUILDS).getLong("player chunk update (ticks)"));
    }

    HashMap<GPlayer, Location> playerLastLocations = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<GPlayer, Location> playerLastChunks : this.playerLastLocations.entrySet()) {
            GPlayer player = playerLastChunks.getKey();
            Player playerPlayer = player.getPlayer();
            Location lastLocation = playerLastChunks.getValue();
            Location newLocation = playerPlayer.getLocation();
            Chunk lastChunk = lastLocation.getChunk();
            Chunk newChunk = newLocation.getChunk();

            if (lastChunk == newChunk) continue;

            // Player moved into new chunk

            // Call event
            PlayerChunkUpdateEvent playerChunkUpdateEvent = new PlayerChunkUpdateEvent(player, lastChunk, newChunk);
            playerChunkUpdateEvent.run();

            if (playerChunkUpdateEvent.isCancelled()) {
                playerPlayer.teleport(lastLocation);
            } else {
                // Update last location
                this.playerLastLocations.replace(player, newLocation);
            }
        }
    }

    @EventHandler
    public void addPlayerOnLogin(GPlayerLoginEvent event) {
        GPlayer gPlayer = event.getGPlayer();
        Player player = event.getPlayer();
        this.playerLastLocations.put(gPlayer, player.getLocation());
    }

    @EventHandler
    public void removePlayerOnLogout(GPlayerQuitEvent event) {
        GPlayer player = event.getGPlayer();
        this.playerLastLocations.remove(player);
    }
}
