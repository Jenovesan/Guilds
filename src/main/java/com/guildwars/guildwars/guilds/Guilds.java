package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Data;
import com.guildwars.guildwars.utils.util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Guilds {

    public static Set<Guild> guilds = new HashSet<>();

    public static void loadGuilds() {
        for (String guildId : Data.get().getKeys(false)) {
            ConfigurationSection guildData = Data.get().getConfigurationSection(guildId);
            assert guildData != null;
            Integer id = Integer.parseInt(guildId);
            String name = guildData.getString("name");
            String description = guildData.getString("description");

            Map<String, Object> playersData = guildData.getConfigurationSection("players").getValues(false);
            HashMap<UUID, Guild.Rank> players = new HashMap<>();
            for (Map.Entry<String, Object> entry : playersData.entrySet()) {
                players.put(UUID.fromString(entry.getKey()), Guild.Rank.valueOf((String) entry.getValue()));
            }

            Map<String, Object> permissionsData = guildData.getConfigurationSection("permissions").getValues(false);
            HashMap<Guild.Permission, Guild.Rank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> entry : permissionsData.entrySet()) {
                permissions.put(Guild.Permission.valueOf(entry.getKey()), Guild.Rank.valueOf((String) entry.getValue()));
            }

            new Guild(id, name, description, players, permissions);
        }
    }

    public static Integer getNewGuildId() {
        int newestGuildId = 0;
        for (Guild guild : getGuilds()) {
            int guildId = guild.getId();
            if (guildId > newestGuildId) {
                newestGuildId = guildId;
            }
        }
        return newestGuildId;
    }

    public static void saveGuildData(Guild guild) {
        Data.get().createSection(String.valueOf(guild.getId()));
        ConfigurationSection guildSection = Data.get().getConfigurationSection(String.valueOf(guild.getId()));
        assert guildSection != null;
        guildSection.set("name", guild.getName());
        guildSection.set("description", guild.getDescription());
        guildSection.set("players", util.hashMapToHashMapString(guild.getPlayers()));
        guildSection.set("permissions", util.hashMapToHashMapString(guild.getPermissions()));
        Data.save();
    }

    public static void removeGuildData(int guildId) {
        Data.get().set(String.valueOf(guildId), null);
        Data.save();
    }

    public static Set<Guild> getGuilds() {
        return guilds;
    }

    public static void removeGuild(Guild guild) {
        getGuilds().remove(guild);
    }

    public static void addGuild(Guild guild) {
        getGuilds().add(guild);
    }

    public static HashSet<String> getAllGuildNames() {
        HashSet<String> guildNames = new HashSet<>();
        for (Guild guild : getGuilds()) {
            System.out.println(guild.getId());
            guildNames.add(guild.getName());
        }
        return guildNames;
    }

    public static Guild get(int guildId) {
        for (Guild guild : getGuilds()) {
            if (guild.getId() == guildId) {
                return guild;
            }
        }
        return null;
    }

    public static Guild get(String guildName) {
        for (Guild guild : getGuilds()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                return guild;
            }
        }
        return null;
    }

}
