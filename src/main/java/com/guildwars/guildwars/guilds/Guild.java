package com.guildwars.guildwars.guilds;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Guild {

    public int id;
    public String name;
    public String description = "None";
    public int power = 10;
    public List<Integer> enemies = new ArrayList<Integer>();
    public Set<Chunk> claims = new HashSet<Chunk>();
    public HashMap<UUID, String> titles = new HashMap<UUID, String>();
    public HashMap<UUID, Rank> players = new HashMap<UUID, Rank>();
    public Location home;

    public Guild(Player player, String guildName, String guildDescription) {
        this.id = 0;
        this.name = guildName;
        this.description = guildDescription;
        if (player != null) {
            this.players.put(player.getUniqueId(), Rank.LEADER);
        }
    }

//    public Guild(int id, String name, String description, int power, ArrayList<Integer> enemies, Set<Chunk> claims, HashMap<UUID, String> titles, HashMap<UUID, Rank> players, Location home) {
//
//    }

    public static enum Rank {
        RECRUIT,
        MEMBER,
        MOD,
        COLEADER,
        LEADER
    }
}

