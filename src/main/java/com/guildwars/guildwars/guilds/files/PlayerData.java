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

        // uuid
        playerData.put("uuid", String.valueOf(player.getUUID()));
        // guild rank
        if (player.getGuildRank() != null) {
            playerData.put("guildRank", player.getGuildRank().name());
        } else {
            playerData.put("guildRank", "null");
        }
        // name
        playerData.put("name", player.getName());
        // power
        playerData.put("power", player.getPower());

        super.saveRaw(player.getUUID(), playerData);
    }
}
