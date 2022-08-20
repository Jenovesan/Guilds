package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.NewgPlayerEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
            float power = (float) playerSection.getDouble("power");

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
        Player playerPlayer = event.getPlayer();
        gPlayer player = gPlayersIndex.getgPlayerByUUID(playerPlayer.getUniqueId());

        // gPlayer for player does not exist
        if (player == null) {
            // Create new gPlayer
            gPlayer newgPlayer = new gPlayer(playerPlayer);
            // Add new gPlayer to gPlayer collection
            getGPlayers().put(playerPlayer.getUniqueId(), newgPlayer);
            // Call Event
            Bukkit.getServer().getPluginManager().callEvent(new NewgPlayerEvent(newgPlayer));
        } else {
            // Set their gPlayer's player
            player.setPlayer(playerPlayer);

            // Update gPlayer name in case they changed their name
            player.setName(playerPlayer.getName());
        }
    }
}
