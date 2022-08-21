package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.GPlayerLoginEvent;
import com.guildwars.guildwars.utils.util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class gPlayersIndex extends Index<gPlayer>{

    public static gPlayersIndex i = new gPlayersIndex();
    public static gPlayersIndex get() {
        return i;
    }

    // -------------------------------------------- //
    // UUID -> gPlayer
    // -------------------------------------------- //

    private HashMap<UUID, gPlayer> id2gPlayer = new HashMap<>();

    public gPlayer getByUUID(UUID uuid) {
        return id2gPlayer.get(uuid);
    }


    // -------------------------------------------- //
    // Player -> gPlayer
    // -------------------------------------------- //

    private HashMap<Player, gPlayer> player2gPlayer = new HashMap<>(); // Raidable, raiding

    public gPlayer getByPlayer(Player player) {
        return player2gPlayer.get(player);
    }


    @EventHandler
    public void updategPlayerOnLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player2gPlayer.remove(player);
    }

    // -------------------------------------------- //
    // On gPlayer Login
    // -------------------------------------------- //

    @EventHandler
    public void updategPlayerNameOnLogin(GPlayerLoginEvent event) {
        Player playerPlayer = event.getPlayer();
        gPlayer player = event.getGPlayer();
        String playerName = playerPlayer.getName().toLowerCase();

        // Player -> gPlayer
        player2gPlayer.put(playerPlayer, player);

        // New gPlayer is created
        if (event.isNewPlayer()) {
            id2gPlayer.put(player.getUUID(), player);
            name2Obj.put(playerName, player);
            return;
        }

        // If Player Name does not match the gPlayer.
        if (name2Obj.get(playerName) != player) {
            String oldName = (String) util.getSingleKeyByValue(name2Obj, player);
            name2Obj.remove(oldName);
            name2Obj.put(playerName, player);
        }
    }

    // -------------------------------------------- //
    // On load
    // -------------------------------------------- //

    @Override
    public void load() {
        for (gPlayer player : gPlayers.getgInstance().getAll()) {
            // UUID -> gPlayer
            id2gPlayer.put(player.getUUID(), player);
            // Name -> gPlayer
            name2Obj.put(player.getName().toLowerCase(), player);
        }
    }

}
