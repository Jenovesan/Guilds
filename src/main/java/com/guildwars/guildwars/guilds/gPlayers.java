package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class gPlayers implements Listener {

    private static HashMap<UUID, gPlayer> gPlayers = new HashMap<>();

    public static void loadGPlayers() {
        for (String playerUUID : PlayerData.get().getKeys(false)) {
            UUID uuid = UUID.fromString(playerUUID);

            ConfigurationSection playerSection = PlayerData.get().getConfigurationSection(playerUUID);
            assert playerSection != null;
            int guildId = playerSection.getInt("guildId");
            GuildRank guildRank = playerSection.getString("guildRank") != null ? GuildRank.valueOf(playerSection.getString("guildRank")) : null;
            String name = playerSection.getString("name");
            int power = playerSection.getInt("power");

            getGPlayers().put(uuid, new gPlayer(uuid, guildId, guildRank, name, power));
        }
    }

    public static void loadGPlayersGuilds() {
        for (Guild guild : Guilds.getAllGuilds()) {
            Set<gPlayer> players = guild.getPlayers().keySet();
            for (gPlayer player : players) {
                player.setGuild(guild);
            }
        }
    }

    private static HashMap<UUID, gPlayer> getGPlayers() {
        return gPlayers;
    }

    public static Collection<gPlayer> getAllGPlayers() {
        return gPlayers.values();
    }

    public static gPlayer get(UUID uuid) {
        return getGPlayers().get(uuid);
    }

    public static gPlayer get(Player player) {
        return getGPlayers().get(player.getUniqueId());
    }

    public static gPlayer get(String name) {
        for (gPlayer player : getAllGPlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        // Create new gPlayer if player is new to server
        Player player = event.getPlayer();
        if (getGPlayers().get(player.getUniqueId()) == null) {
            getGPlayers().put(player.getUniqueId(), new gPlayer(player));
            return;
        }

        gPlayer gPlayer = get(player);

        // Set their gPlayer's player
        gPlayer.setPlayer(player);


        // Update gPlayer name in case they changed their name
        get(player).setName(player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from gPlayer
        get(event.getPlayer()).setPlayer(null);
    }
}
