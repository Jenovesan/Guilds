package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class Guilds extends Coll<Guild> {

    public static Guilds i = new Guilds();
    public static Guilds get() {
        return i;
    }

    @Override
    public void loadAll() {
        List<FileConfiguration> guildData = GuildData.get().getAllData();
        for (FileConfiguration dataFile : guildData) {

            // id
            String id = dataFile.getString("id");
            // name
            String name = dataFile.getString("name");
            // description
            String description = dataFile.getString("description");
            // players
            List<String> rawPlayers = dataFile.getStringList("players");
            HashSet<gPlayer> players = new HashSet<>();
            for (String uuid : rawPlayers) {
                players.add(gPlayersIndex.get().getByUUID(UUID.fromString(uuid)));
            }
            // permissions
            Map<String, Object> rawPermissions = dataFile.getConfigurationSection("permissions").getValues(false);
            HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
            for (Map.Entry<String, Object> permission : rawPermissions.entrySet()) {
                permissions.put(GuildPermission.valueOf(permission.getKey()), GuildRank.valueOf((String) permission.getValue()));
            }
            // claim locations
            List<String> claimLocationsRaw = dataFile.getStringList("claimLocations");
            HashSet<int[]> claimLocations = new HashSet<>();
            for (String location : claimLocationsRaw) {
                String[] locationsStrings = location.split(":");
                int[] locations = new int[] {Integer.parseInt(locationsStrings[0]), Integer.parseInt(locationsStrings[1])};
                claimLocations.add(locations);
            }
            // raid end time
            long raidEndTime = dataFile.getLong("raidEndTime");

            // home
            Location home = dataFile.getLocation("home");

            // create guild
            Guild newGuild = new Guild(id, name, description, players, permissions, claimLocations, raidEndTime, home);

            // add guild
            getAll().add(newGuild);
        }
    }

    @Override
    public void loadGuilds() {
        for (Guild guild : getAll()) {
            // get the guild's data
            ConfigurationSection dataFile = YamlConfiguration.loadConfiguration(new File(GuildData.get().getDataFolderPath() + "/" + guild.getId()));

            // add enemies
            List<String> enemiesRaw = dataFile.getStringList("enemies");
            HashSet<Guild> enemies = new HashSet<>();
            for (String id : enemiesRaw) {
                enemies.add(GuildsIndex.get().getById(id));
            }
            enemies.forEach(guild::addEnemy);

            // add raided by
            String raidedById = dataFile.getString("raidedBy");
            guild.setRaidedBy(GuildsIndex.get().getById(raidedById));
        }
    }
}
