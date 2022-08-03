package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class GuildsFastData implements Listener {

    public static HashMap<Player, Integer> playersGuildsIds = new HashMap<>();

    public static void loadPlayersGuildsIds() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Guild playerGuild =  gUtil.getOfflinePlayerGuild(player.getUniqueId());
            putPlayer(player, playerGuild);
        }
    }

    public static HashMap<Player, Integer> getPlayersGuildsIds() {
        return playersGuildsIds;
    }

    private static void putPlayer(Player player, Guild guild) {
        if (guild != null) {
            getPlayersGuildsIds().put(player, guild.getId());
        } else {
            getPlayersGuildsIds().put(player, null);
        }
    }

    @EventHandler
    public void updatePlayerOnGuildChange(PlayerGuildChangeEvent event) {
        Player player = event.getPlayer();
        Guild newGuild = event.getNewGuild();
        putPlayer(player, newGuild);
    }

    @EventHandler
    public void addPlayerDataOnLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Guild playerGuild =  gUtil.getOfflinePlayerGuild(player.getUniqueId());
        putPlayer(player, playerGuild);
    }

    @EventHandler
    public void removePlayerDataOnLogout(PlayerQuitEvent event) {
        getPlayersGuildsIds().remove(event.getPlayer());
    }
}