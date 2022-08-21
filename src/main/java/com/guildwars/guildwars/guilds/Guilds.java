package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.utils.util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.stream.Collectors;

public class Guilds extends Coll<Guild> implements Listener {

    public static Guilds i = new Guilds();
    public static Guilds get() {
        return i;
    }

    @Override
    public void load() {
        for (String guildId : GuildData.get().getKeys(false)) {
            ConfigurationSection guildData = GuildData.get().getConfigurationSection(guildId);
            String name = guildData.getString("name");
            String description = guildData.getString("description");

            Map<String, Object> playersData = guildData.getConfigurationSection("players").getValues(false);
            HashMap<gPlayer, GuildRank> players = new HashMap<>();
            for (Map.Entry<String, Object> entry : playersData.entrySet()) {
                UUID memberUUID = UUID.fromString(entry.getKey());
                for (gPlayer player : gPlayers.getgInstance().getAll()) {
                    if (player.getUUID() == memberUUID) {
                        players.put(player, GuildRank.valueOf((String) entry.getValue()));
                    }
                }
            }

            Map<String, Object> permissionsData = guildData.getConfigurationSection("permissions").getValues(false);
            HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> entry : permissionsData.entrySet()) {
                permissions.put(GuildPermission.valueOf(entry.getKey()), GuildRank.valueOf((String) entry.getValue()));
            }

            long raidEndTime = guildData.getLong("raidEndTime");

            Guild loadedGuild = new Guild(guildId, name, description, players, permissions, raidEndTime);
            add(loadedGuild);
        }
    }

    @Override
    public void loadGuilds() {
        for (Guild guild : getAll()) {
            ConfigurationSection guildData = GuildData.get().getConfigurationSection(guild.getId().toString());

            // Enemies
            List<String> enemiesIds = guildData.getStringList("enemies");
            for (String id : enemiesIds) {
                guild.addEnemy((GuildsIndex.get().getById(id)));
            }

            // RaidedBy
            String raidedById = guildData.getString("raidedBy");
            guild.setRaidedBy(GuildsIndex.get().getById(raidedById));
        }
    }

    @Override
    public void save(Guild guild) {
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
        guildSection.set("enemies", guild.getEnemies().stream().map(Guild::getId).collect(Collectors.toList()));
        guildSection.set("raidedBy", guild.getRaidedBy().getId());
        guildSection.set("raidEndTime", guild.getRaidEndTime());
        GuildData.save();
    }

    public void removeGuildData(Guild guild) {
        GuildData.get().set(guild.getId(), null);
        GuildData.save();
    }

    @EventHandler
    public void updateGuildEnemiesOnGuildDisband(GuildDisbandEvent event) {
        Guild disbandedGuild = event.getGuild();
        for (Guild guild : getAll()) {
            guild.getEnemies().remove(disbandedGuild);
        }
    }
}
