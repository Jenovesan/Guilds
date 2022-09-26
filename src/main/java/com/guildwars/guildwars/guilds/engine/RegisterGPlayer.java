package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.Indexing;
import com.guildwars.guildwars.guilds.event.GPlayerQuitEvent;
import com.guildwars.guildwars.guilds.event.GPlayerLoginEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.GPlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegisterGPlayer extends Engine {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GPlayer gPlayer = Indexing.get().getGPlayerByUUID(player.getUniqueId());

        // gPlayer for gPlayer does not exist
        if (gPlayer == null) {
            gPlayer = createNewgPlayer(player);
        } else {
            updateGPlayer(player, gPlayer);
        }

        // Call event
        GPlayerLoginEvent gPlayerLoginEvent = new GPlayerLoginEvent(player, gPlayer);
        gPlayerLoginEvent.run();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Call event
        GPlayer gPlayer = Indexing.get().getGPlayerByUUID(player.getUniqueId());
        GPlayerQuitEvent gPlayerQuitEvent = new GPlayerQuitEvent(gPlayer, player);
        gPlayerQuitEvent.run();
    }

    private GPlayer createNewgPlayer(Player player) {
        // Create new gPlayer
        GPlayer gPlayer = new GPlayer(player);

        // Add new gPlayer to gPlayer collection
        GPlayers.get().add(gPlayer);

        // Save data
        PlayerData.get().save(gPlayer);

        // Add to index
        Indexing.get().add(gPlayer);

        return gPlayer;
    }

    private void updateGPlayer(Player player, GPlayer gPlayer) {
        // Set their gPlayer's player
        gPlayer.setPlayer(player);

        String gPlayerName = gPlayer.getName();
        String playerName = player.getName();

        // Update gPlayer name because player changed their name
        if (!gPlayerName.equals(playerName)) {
            // Set name
            gPlayer.setName(playerName);

            // Save data
            PlayerData.get().save(gPlayer);

            // Update index
            Indexing.get().updateName(gPlayer, gPlayerName, playerName);
        }
    }
}
