package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.NewgPlayerEvent;
import com.guildwars.guildwars.utils.util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class gPlayersIndex {

    // -------------------------------------------- //
    // UUID -> gPlayer
    // -------------------------------------------- //

    private static HashMap<UUID, gPlayer> uuid2gPlayer = new HashMap<>(); // Raidable, raiding

    private static HashMap<UUID, gPlayer> getUUID2gPlayer() {
        return uuid2gPlayer;
    }

   public static gPlayer getgPlayerByUUID(UUID uuid) {
        return getUUID2gPlayer().get(uuid);
   }

    // -------------------------------------------- //
    // Name -> gPlayer
    // -------------------------------------------- //

    private static HashMap<String, gPlayer> name2gPlayer = new HashMap<>(); // Raidable, raiding

    private static HashMap<String, gPlayer> getName2gPlayer() {
        return name2gPlayer;
    }

    public static gPlayer getgPlayerByName(String name) {
        return getName2gPlayer().get(name);
    }

    @EventHandler
    public void updategPlayerNameOnLogin(PlayerJoinEvent event) {
        Player playerPlayer = event.getPlayer();
        gPlayer player = getgPlayerByUUID(playerPlayer.getUniqueId());
        String playerName = playerPlayer.getName().toLowerCase();

        // Player's name hasn't changed
        if (getgPlayerByName(playerName) == player) return;

        // Player changed their name
        String oldName = (String) util.getSingleKeyByValue(getName2gPlayer(), player);
        getName2gPlayer().remove(oldName);
        getName2gPlayer().put(playerName, player);
    }

    // -------------------------------------------- //
    // Player -> gPlayer
    // -------------------------------------------- //

    private static HashMap<Player, gPlayer> player2gPlayer = new HashMap<>(); // Raidable, raiding

    private static HashMap<Player, gPlayer> getPlayer2gPlayer() {
        return player2gPlayer;
    }

    public static gPlayer getgPlayerByPlayer(Player player) {
        return getPlayer2gPlayer().get(player);
    }

    @EventHandler
    public void updategPlayerOnLogin(PlayerJoinEvent event) {
        Player playerPlayer = event.getPlayer();
        UUID playerUUID = playerPlayer.getUniqueId();
        gPlayer player = getgPlayerByUUID(playerUUID);
        getPlayer2gPlayer().put(playerPlayer, player);
    }

    @EventHandler
    public void updategPlayerOnLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getPlayer2gPlayer().remove(player);
    }

    // -------------------------------------------- //
    // Add gPlayer when new gPlayer created
    // -------------------------------------------- //

    @EventHandler
    public void addgPlayerOnNewgPlayerEvent(NewgPlayerEvent event) {
        gPlayer player = event.getPlayer();

        // UUID -> gPlayer
        getUUID2gPlayer().put(player.getUUID(), player);
        // Name -> gPlayer
        getName2gPlayer().put(player.getName().toLowerCase(), player);
    }

    // -------------------------------------------- //
    // On load
    // -------------------------------------------- //

    public static void load() {
        for (gPlayer player : gPlayers.getAllGPlayers()) {
            // UUID -> gPlayer
            getUUID2gPlayer().put(player.getUUID(), player);
            // Name -> gPlayer
            getName2gPlayer().put(player.getName().toLowerCase(), player);
        }
    }

}
