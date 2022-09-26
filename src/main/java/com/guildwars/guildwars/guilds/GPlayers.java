package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class GPlayers extends Coll<GPlayer> {

    public static GPlayers instance = new GPlayers();
    public static GPlayers get() {
        return instance;
    }

    @Override
    public void load() {
        List<FileConfiguration> playerData = PlayerData.get().getAllData();
        for (FileConfiguration dataFile : playerData) {

            String rawUUID = dataFile.getString("uuid");
            UUID uuid = UUID.fromString(rawUUID);

            // Guild
            String guildId = dataFile.getString("guildId");
            Guild guild = Guilds.get().getById(guildId);

            GuildRank guildRank = dataFile.contains("guildRank") ? GuildRank.valueOf(dataFile.getString("guildRank")) : null;
            String name = dataFile.getString("name");
            float power = (float) dataFile.getDouble("power");

            GPlayer newPlayer = new GPlayer(uuid, guild, guildRank, name, power);

            getAll().add(newPlayer);
        }
    }
}
