package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Guilds extends Coll<Guild> {

    public static Guilds i = new Guilds();
    public static Guilds get() {
        return i;
    }

    @Override
    public void load() {
        List<FileConfiguration> guildData = GuildData.get().getAllData();
        for (FileConfiguration dataFile : guildData) {

            // id
            String id = dataFile.getString("id");

            // name
            String name = dataFile.getString("name");

            // description
            String description = dataFile.getString("description");

            // permissions
            Map<String, Object> rawPermissions = dataFile.getConfigurationSection("permissions").getValues(false);
            HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> permission : rawPermissions.entrySet()) {
                permissions.put(GuildPermission.valueOf(permission.getKey()), GuildRank.valueOf((String) permission.getValue()));
            }

            // relation wishes
            HashMap<Guild, Relation> relationWishes = new HashMap<>();
            if (dataFile.isConfigurationSection("relationWishes")) {
                // add relations
                Map<String, Object> rawRelationWishes = dataFile.getConfigurationSection("relationWishes").getValues(false);

                for (Map.Entry<String, Object> relationWish : rawRelationWishes.entrySet()) {
                    Guild relationWith = getById(relationWish.getKey());
                    Relation relation = Relation.valueOf((String) relationWish.getValue());
                    relationWishes.put(relationWith, relation);
                }
            }

            // raided by
            String raidedById = dataFile.getString("raidedBy");
            Guild raidedBy = getById(raidedById);

            // raid end time
            long raidEndTime = dataFile.getLong("raidEndTime");

            // home
            Location home = dataFile.getLocation("home");

            // create guild
            Guild newGuild = new Guild(id, name, description, permissions, relationWishes, raidedBy, raidEndTime, home);

            // add guild
            getAll().add(newGuild);
        }
    }

    public Guild getById(String id) {
        // This function is not meant to be fast because it will only be used when loading/unloading the plugin
        for (Guild guild : getAll()) {
            if (guild.getId().equals(id)) {
                return guild;
            }
        }
        return null;
    }
}
