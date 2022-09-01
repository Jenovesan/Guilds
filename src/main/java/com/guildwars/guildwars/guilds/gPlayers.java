package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class gPlayers extends Coll<gPlayer> {

    public static gPlayers instance = new gPlayers();
    public static gPlayers get() {
        return instance;
    }

    @Override
    public void loadAll() {
        List<FileConfiguration> playerData = PlayerData.get().getAllData();
        for (FileConfiguration dataFile : playerData) {

            String uuid = dataFile.getString("uuid");
            GuildRank guildRank = GuildRank.valueOf(dataFile.getString("guildRank"));
            String name = dataFile.getString("name");
            float power = (float) dataFile.getDouble("power");

            gPlayer newPlayer = new gPlayer(uuid, guildRank, name, power);

            getAll().add(newPlayer);
        }
    }

    @Override
    public void loadGuilds() {
        for (Guild guild : Guilds.get().getAll()) {
            HashSet<gPlayer> players = guild.getPlayers();
            for (gPlayer player : players) {
                player.setGuild(guild);
            }
        }
    }
}
