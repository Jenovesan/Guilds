package com.guildwars.guildwars.guilds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Objects;

public class gPlayers implements Listener {

    private static HashMap<Player, gPlayer> gPlayers = new HashMap<>();

    public static void loadGPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addGPlayer(player);
        }
    }

    private static HashMap<Player, gPlayer> getGPlayers() {
        return gPlayers;
    }

    private static void addGPlayer(Player player) {
        getGPlayers().put(player, getNewGPlayer(player));
    }

    private static void removeGPlayer(Player player) {
        getGPlayers().remove(player);
    }

    public static gPlayer get(Player player) {
        return getGPlayers().get(player);
    }

    private static gPlayer getNewGPlayer(Player player) {
        int guildId = Objects.requireNonNullElse(GuildsFastData.getPlayersGuildsIds().get(player), -1);
        Guild playerGuild = Guilds.get(guildId);
        return new gPlayer(player, playerGuild);
    }

    @EventHandler
    public void addGPlayerOnLogin(PlayerJoinEvent event) {
        addGPlayer(event.getPlayer());
        event.getPlayer().sendMessage(String.valueOf(getGPlayers()));
    }

    @EventHandler
    public void removeGPlayerOnLogout(PlayerQuitEvent event) {
        removeGPlayer(event.getPlayer());
    }
}
