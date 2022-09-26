package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.ObjectDataManager;
import com.guildwars.guildwars.entity.GPlayer;

import java.util.HashMap;

public class PlayerData extends ObjectDataManager<GPlayer> {

    public static PlayerData instance = new PlayerData();
    public static PlayerData get() {
        return instance;
    }

    public PlayerData() {
        super("playerdata");
    }

    @Override
    public void save(GPlayer gPlayer) {
        HashMap<String, Object> playerData = new HashMap<>();

        // uuid
        playerData.put("uuid", String.valueOf(gPlayer.getUUID()));

        // guildId
        playerData.put("guildId", gPlayer.isInGuild() ? gPlayer.getGuild().getId() : null);

        // guild rank
        playerData.put("guildRank", gPlayer.getGuildRank() != null ? gPlayer.getGuildRank().name() : null);

        // name
        playerData.put("name", gPlayer.getName());

        // power
        playerData.put("power", gPlayer.getPower());

        super.saveRaw(String.valueOf(gPlayer.getUUID()), playerData);
    }
}
