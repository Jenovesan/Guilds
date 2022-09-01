package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.ObjectDataManager;
import com.guildwars.guildwars.guilds.gPlayer;

import java.util.HashMap;

public class PlayerData extends ObjectDataManager<gPlayer> {

    public static PlayerData instance = new PlayerData();
    public static PlayerData get() {
        return instance;
    }

    public PlayerData() {
        super("playerdata");
    }

    @Override
    public void save(gPlayer player) {
        HashMap<String, Object> playerData = new HashMap<>();

        playerData.put("uuid", String.valueOf(player.getUUID()));
        playerData.put("guildRank", player.getGuildRank());
        playerData.put("name", player.getName());
        playerData.put("power", player.getPower());

        saveRaw(player.getUUID(), playerData);
    }
}
