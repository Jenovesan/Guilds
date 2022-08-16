package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.utils.util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class Guilds implements Listener {

    public static Set<Guild> guilds = new HashSet<>();

    public static void loadGuilds() {
        for (String guildId : GuildData.get().getKeys(false)) {
            ConfigurationSection guildData = GuildData.get().getConfigurationSection(guildId);
            assert guildData != null;
            Integer id = Integer.parseInt(guildId);
            String name = guildData.getString("name");
            String description = guildData.getString("description");

            Map<String, Object> playersData = guildData.getConfigurationSection("players").getValues(false);
            HashMap<gPlayer, GuildRank> players = new HashMap<>();
            for (Map.Entry<String, Object> entry : playersData.entrySet()) {
                players.put(gPlayers.get(UUID.fromString(entry.getKey())), GuildRank.valueOf((String) entry.getValue()));
            }

            Map<String, Object> permissionsData = guildData.getConfigurationSection("permissions").getValues(false);
            HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> entry : permissionsData.entrySet()) {
                permissions.put(GuildPermission.valueOf(entry.getKey()), GuildRank.valueOf((String) entry.getValue()));
            }

            HashSet<Integer> enemies = new HashSet<>(guildData.getIntegerList("enemies"));

            boolean adminGuild = guildData.getBoolean("adminGuild");

            Guild loadedGuild = new Guild(id, name, description, players, permissions, enemies, adminGuild);
            addGuild(loadedGuild);
        }

        createAdminGuilds();
    }

    private static Guild border;

    public static Guild getBorder() {
        return border;
    }

    private static void createAdminGuilds() {
        // Make sure border guild exists
        if (get("border") == null) {
            //Create guild for border
            Guild borderGuild = new Guild(null, "Border", "None");
            borderGuild.setAdminGuild(Config.get().getString("border guild color"));
            addGuild(borderGuild);
            border = borderGuild;
        }
    }

    public static Integer getNewGuildId() {
        int newestGuildId = 0;
        for (Guild guild : getAllGuilds()) {
            int guildId = guild.getId();
            System.out.println(guildId);
            if (guildId > newestGuildId) {
                newestGuildId = guildId;
            }
        }
        return newestGuildId + 1;
    }

    public static void saveGuildData(Guild guild) {
        GuildData.get().createSection(String.valueOf(guild.getId()));
        ConfigurationSection guildSection = GuildData.get().getConfigurationSection(String.valueOf(guild.getId()));
        assert guildSection != null;
        guildSection.set("name", guild.getName());
        guildSection.set("description", guild.getDescription());
        HashMap<String, String> players = new HashMap<>();
        for (Map.Entry<gPlayer, GuildRank> entry : guild.getPlayers().entrySet()) {
            players.put(String.valueOf(entry.getKey().getUUID()), entry.getValue().name());
        }
        guildSection.set("players", players);
        guildSection.set("permissions", util.hashMapToHashMapString(guild.getPermissions()));
        guildSection.set("enemies", List.copyOf(guild.getEnemies()));
        guildSection.set("adminGuild", guild.isAdminGuild());
        GuildData.save();
    }

    public static void removeGuildData(int guildId) {
        GuildData.get().set(String.valueOf(guildId), null);
        GuildData.save();
    }

    public static Set<Guild> getAllGuilds() {
        return guilds;
    }

    public static void removeGuild(Guild guild) {
        getAllGuilds().remove(guild);
    }

    public static void addGuild(Guild guild) {
        getAllGuilds().add(guild);
    }

    public static Guild get(int guildId) {
        for (Guild guild : getAllGuilds()) {
            if (guild.getId() == guildId) {
                return guild;
            }
        }
        return null;
    }

    public static Guild get(String guildName) {
        for (Guild guild : getAllGuilds()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                return guild;
            }
        }
        return null;
    }

    @EventHandler
    public void updateGuildEnemiesOnGuildDisband(GuildDisbandEvent event) {
        Integer disbandedGuildId = event.getGuild().getId();
        for (Guild guild : getAllGuilds()) {
            guild.getEnemies().remove(disbandedGuildId);
        }
    }
}
