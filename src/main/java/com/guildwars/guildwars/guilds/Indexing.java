package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Guild;
import org.bukkit.entity.Player;

import java.util.*;

public class Indexing {

    private static Indexing i = new Indexing();
    public static Indexing get() {
        return i;
    }

    // -------------------------------------------- //
    // Load
    // -------------------------------------------- //

    public void load() {
        // Guilds
        Guilds.get().getAll().forEach(this::add);
        // GPlayers
        GPlayers.get().getAll().forEach(this::add);
    }


    // -------------------------------------------- //
    // Guilds
    // -------------------------------------------- //

    // Initialize

    private final HashMap<String, Guild> name2Guild = new HashMap<>();
    private final HashMap<Guild, HashSet<GPlayer>> guild2Players = new HashMap<>();
    private final HashMap<Player, Guild> player2Guild = new HashMap<>();

    // Getters

    public Guild getGuildByName(String name) {
        name = formatName(name);
        System.out.println(Indexing.get().name2Guild);
        return name2Guild.get(name);
    }

    public HashSet<GPlayer> getGuildPlayers(Guild guild) {
        return guild2Players.get(guild);
    }

    public Guild getGuildByPlayer(Player player) {
        return player2Guild.get(player);
    }

    // Add to index

    public void add(Guild guild) {
        name2Guild.put(formatName(guild.getName()), guild);
        guild2Players.put(guild, new HashSet<>());
    }

    // Update index

    public void updateName(Guild guild, String oldName, String newName) {
        name2Guild.remove(formatName(oldName));
        name2Guild.put(formatName(newName), guild);
    }

    public void addPlayerToGuild(Guild guild, GPlayer gPlayer) {
        HashSet<GPlayer> gPlayers = guild2Players.get(guild);
        gPlayers.add(gPlayer);
        guild2Players.replace(guild, gPlayers);
    }

    public void removePlayerFromGuild(Guild guild, GPlayer gPlayer) {
        HashSet<GPlayer> gPlayers = guild2Players.get(guild);
        gPlayers.remove(gPlayer);
        guild2Players.replace(guild, gPlayers);
    }

    // Remove from index
    public void remove(Guild guild) {
        name2Guild.remove(guild.getName());
        guild2Players.remove(guild);
    }

    // -------------------------------------------- //
    // GPlayers
    // -------------------------------------------- //

    // Initialize

    HashMap<String, GPlayer> name2GPlayer = new HashMap<>();
    HashMap<UUID, GPlayer> uuid2GPlayer = new HashMap<>();

    // Getters

    public GPlayer getGPlayerByName(String name) {
        name = formatName(name);
        return name2GPlayer.get(name);
    }

    public GPlayer getGPlayerByUUID(UUID uuid) {
        return uuid2GPlayer.get(uuid);
    }

    // Add to index

    public void add(GPlayer gPlayer) {
        name2GPlayer.put(formatName(gPlayer.getName()), gPlayer);
        uuid2GPlayer.put(gPlayer.getUUID(), gPlayer);
        player2Guild.put(gPlayer.getPlayer(), gPlayer.getGuild());
        if (gPlayer.isInGuild()) addPlayerToGuild(gPlayer.getGuild(), gPlayer);
    }

    public void add(Player player, Guild guild) {
        player2Guild.put(player, guild);
    }

    // Remove from index

    public void remove(Player player) {
        player2Guild.remove(player);
    }

    // Update index

    public void updateName(GPlayer gPlayer, String oldName, String newName) {
        name2GPlayer.remove(formatName(oldName));
        name2GPlayer.put(formatName(newName), gPlayer);
    }

    public void updatePlayerGuild(Player player, Guild newGuild) {
        if (newGuild != null) player2Guild.replace(player, newGuild);
        else player2Guild.remove(player);
    }

    // -------------------------------------------- //
    // Misc
    // -------------------------------------------- //

    private String formatName(String name) {
        return name.toLowerCase();
    }
}
