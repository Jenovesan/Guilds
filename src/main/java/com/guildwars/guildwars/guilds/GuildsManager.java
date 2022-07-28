package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Data;
import com.guildwars.guildwars.utils.util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class GuildsManager {

    public static Set<Guild> guilds = new HashSet<Guild>();

    public static void loadGuilds() {
        for (String guildId : Data.get().getKeys(false)) {
            ConfigurationSection guildData = Data.get().getConfigurationSection(guildId);
            assert guildData != null;
            Guild guild = new Guild(null, guildData.getString("name"), guildData.getString("description"));
            guild.id = Integer.parseInt(guildId);
            guild.power = guildData.getInt("power");
            guild.enemies = guildData.getIntegerList("enemies");
            guild.claims = null;

            Map<String, Object> titles = guildData.getConfigurationSection("titles").getValues(false);
            for (Map.Entry<String, Object> entry : titles.entrySet()) {
                guild.titles.put(UUID.fromString(entry.getKey()), (String) entry.getValue());
            }

            Map<String, Object> players = guildData.getConfigurationSection("players").getValues(false);
            for (Map.Entry<String, Object> entry : players.entrySet()) {
                guild.players.put(UUID.fromString(entry.getKey()), Guild.Rank.valueOf((String) entry.getValue()));
            }

            guild.home = guildData.getLocation("home");

            getGuilds().add(guild);
        }
    }

    public static void saveGuild(Guild guild) {
        Data.get().createSection(String.valueOf(guild.id));
        ConfigurationSection guildSection = Data.get().getConfigurationSection(String.valueOf(guild.id));
        assert guildSection != null;
        guildSection.set("name", guild.name);
        guildSection.set("description", guild.description);
        guildSection.set("power", guild.power);
        guildSection.set("enemies", guild.enemies);
        guildSection.set("claims", guild.claims);
        guildSection.set("titles", util.hashMapToHashMapString(guild.titles));
        guildSection.set("players", util.hashMapToHashMapString(guild.players));
        guildSection.set("home", guild.home);
        Data.save();
    }

    public static Set<Guild> getGuilds() {
//        System.out.println(String.valueOf(guilds));
        return guilds;
    }
}
