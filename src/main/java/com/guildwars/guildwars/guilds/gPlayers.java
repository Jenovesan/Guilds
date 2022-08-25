package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class gPlayers extends Coll<gPlayer> {

    public static gPlayers instance = new gPlayers();
    public static gPlayers get() {
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
}
