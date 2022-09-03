package com.guildwars.guildwars.guilds;

import org.bukkit.entity.Player;

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

    public HashMap<Player, gPlayer> getPlayer2Player() {
        return player2gPlayer;
    }

    public gPlayer getByPlayer(Player player) {
        return player2gPlayer.get(player);
    }

    // -------------------------------------------- //
    // Keeping Indexes up-to-date
    // -------------------------------------------- //

    public void update(gPlayer player) {
        Player playerPlayer = player.getPlayer();

        // Player -> gPlayer
        player2gPlayer.put(playerPlayer, player);
    }

    @Override
    public void updateName(gPlayer player, String oldName, String newName) {
        name2Obj.remove(oldName);
        name2Obj.put(newName, player);
    }

    @Override
    public void add(gPlayer player) {
        id2gPlayer.put(UUID.fromString(player.getUUID()), player);
        name2Obj.put(player.getName(), player);
        player2gPlayer.put(player.getPlayer(), player);
    }

    // -------------------------------------------- //
    // On load
    // -------------------------------------------- //

    @Override
    public void load() {
        for (gPlayer player : gPlayers.get().getAll()) {
            // UUID -> gPlayer
            id2gPlayer.put(UUID.fromString(player.getUUID()), player);
            // Name -> gPlayer
            name2Obj.put(player.getName().toLowerCase(), player);
        }
    }
}
