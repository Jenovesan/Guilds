package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Data;
import com.guildwars.guildwars.utils.util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.util.*;

public class Guilds implements Listener {

    public static Set<Guild> guilds = new HashSet<>();

    public static void loadGuilds() {
        for (String guildId : Data.get().getKeys(false)) {
            ConfigurationSection guildData = Data.get().getConfigurationSection(guildId);
            assert guildData != null;
            Integer id = Integer.parseInt(guildId);
            String name = guildData.getString("name");
            String description = guildData.getString("description");

            Map<String, Object> playersData = guildData.getConfigurationSection("players").getValues(false);
            HashMap<UUID, GuildRank> players = new HashMap<>();
            for (Map.Entry<String, Object> entry : playersData.entrySet()) {
                players.put(UUID.fromString(entry.getKey()), GuildRank.valueOf((String) entry.getValue()));
            }

            Map<String, Object> permissionsData = guildData.getConfigurationSection("permissions").getValues(false);
            HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> entry : permissionsData.entrySet()) {
                permissions.put(GuildPermission.valueOf(entry.getKey()), GuildRank.valueOf((String) entry.getValue()));
            }

            HashSet<Integer> enemies = new HashSet<>(guildData.getIntegerList("enemies"));

            new Guild(id, name, description, players, permissions, enemies);
        }
    }

    public static Integer getNewGuildId() {
        int newestGuildId = 0;
        for (Guild guild : getGuilds()) {
            int guildId = guild.getId();
            System.out.println(guildId);
            if (guildId > newestGuildId) {
                newestGuildId = guildId;
            }
        }
        return newestGuildId + 1;
    }

    public static void saveGuildData(Guild guild) {
        Data.get().createSection(String.valueOf(guild.getId()));
        ConfigurationSection guildSection = Data.get().getConfigurationSection(String.valueOf(guild.getId()));
        assert guildSection != null;
        guildSection.set("name", guild.getName());
        guildSection.set("description", guild.getDescription());
        guildSection.set("players", util.hashMapToHashMapString(guild.getPlayers()));
        guildSection.set("permissions", util.hashMapToHashMapString(guild.getPermissions()));
        guildSection.set("enemies", List.copyOf(guild.getEnemies()));
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

    public static boolean guidlNameExists(String name) {
        for (Guild guild : getGuilds()) {
            if (guild.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
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
