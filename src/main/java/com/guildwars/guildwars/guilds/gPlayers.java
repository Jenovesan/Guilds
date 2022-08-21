package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.GPlayerLoginEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class gPlayers extends Coll<gPlayer> implements Listener {

    public static gPlayers instance = new gPlayers();
    public static gPlayers getgInstance() {
        return instance;
    }

    @Override
    public void load() {
        for (String playerUUID : PlayerData.get().getKeys(false)) {
            UUID uuid = UUID.fromString(playerUUID);

            ConfigurationSection playerSection = PlayerData.get().getConfigurationSection(playerUUID);
            assert playerSection != null;
            String name = playerSection.getString("name");
            float power = (float) playerSection.getDouble("power");

            getAll().add(new gPlayer(uuid, name, power));
        }
    }

    @Override
    public void save(gPlayer obj) {

    }

    @Override
    public void loadGuilds() {
        for (Guild guild : Guilds.get().getAll()) {
            Set<gPlayer> players = guild.getPlayers().keySet();
            for (gPlayer player : players) {
                player.setGuild(guild);
                player.setGuildId(guild.getId());
                player.setGuildRank(guild.getGuildRank(player));
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player playerPlayer = event.getPlayer();
        gPlayer player = gPlayersIndex.get().getByUUID(playerPlayer.getUniqueId());
        boolean newPlayer = false;

        // gPlayer for player does not exist
        if (player == null) {
            // Create new gPlayer
            player = new gPlayer(playerPlayer);

            // Add new gPlayer to gPlayer collection
            getAll().add(player);

            // Update newPlayer
            newPlayer = true;
        } else {
            // Set their gPlayer's player
            player.setPlayer(playerPlayer);

            // Update gPlayer name in case they changed their name
            player.setName(playerPlayer.getName());
        }

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new GPlayerLoginEvent(playerPlayer, player, newPlayer));
    }
}
