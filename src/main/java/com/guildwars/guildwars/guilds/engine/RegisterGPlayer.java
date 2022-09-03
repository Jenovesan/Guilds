package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.event.GPlayerQuitEvent;
import com.guildwars.guildwars.guilds.event.GPlayerLoginEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegisterGPlayer extends Engine {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player playerPlayer = event.getPlayer();
        gPlayer player = gPlayersIndex.get().getByUUID(playerPlayer.getUniqueId());

        // gPlayer for player does not exist
        if (player == null) {
            player = createNewgPlayer(playerPlayer);
        } else {
            updategPlayer(playerPlayer, player);
        }

        // Call event
        GPlayerLoginEvent gPlayerLoginEvent = new GPlayerLoginEvent(playerPlayer, player);
        gPlayerLoginEvent.run();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player playerPlayer = event.getPlayer();

        // Call event
        gPlayer player = gPlayersIndex.get().getByPlayer(playerPlayer);
        GPlayerQuitEvent gPlayerQuitEvent = new GPlayerQuitEvent(player);
        gPlayerQuitEvent.run();

        // Remove from index
        gPlayersIndex.get().getPlayer2Player().remove(playerPlayer);
    }

    private gPlayer createNewgPlayer(Player playerPlayer) {
        // Create new gPlayer
        gPlayer player = new gPlayer(playerPlayer);

        // Add new gPlayer to gPlayer collection
        gPlayers.get().add(player);

        // Save data
        PlayerData.get().save(player);

        // Update index
        gPlayersIndex.get().add(player);

        return player;
    }

    private static void updategPlayer(Player playerPlayer, gPlayer player) {
        // Set their gPlayer's player
        player.setPlayer(playerPlayer);

        String gPlayerName = player.getName();
        String playerName = playerPlayer.getName();

        // Update gPlayer name because player changed their name
        if (!gPlayerName.equals(playerName)) {
            // Set name
            player.setName(playerPlayer.getName());

            // Save data
            PlayerData.get().save(player);

            // Update index
            gPlayersIndex.get().updateName(player, gPlayerName, playerName);
        }

        // Update index
        gPlayersIndex.get().update(player);
    }
}
