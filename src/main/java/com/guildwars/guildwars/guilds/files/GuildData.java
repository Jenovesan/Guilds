package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.ObjectDataManager;
import com.guildwars.guildwars.guilds.Relation;
import com.guildwars.guildwars.utils.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GuildData extends ObjectDataManager<Guild> {

    public static GuildData instance = new GuildData();
    public static GuildData get() { return instance; }

    public GuildData() {
        super("guilddata");
    }

    @Override
    public void save(Guild guild) {
        HashMap<String, Object> guildData = new HashMap<>();

        guildData.put("id", guild.getId());
        guildData.put("name", guild.getName());
        guildData.put("description", guild.getDescription());
        guildData.put("permissions", util.hashMapToHashMapString(guild.getPermissions()));
        guildData.put("relationWishes", getStrRelationWishes(guild));
        if (guild.getRaidedBy() != null) guildData.put("raidedBy", guild.getRaidedBy().getId());
        guildData.put("raidEndTime", guild.getRaidEndTime());
        guildData.put("home", guild.getHome());

        saveRaw(guild.getId(), guildData);
    }

    private HashMap<String, String> getStrRelationWishes(Guild guild) {
        HashMap<String, String> strRelationWishes = new HashMap<>();
        for (Map.Entry<Guild, Relation> relationWish : guild.getRelationWishes().entrySet()) {
            String guildId = guild.getId();
            String relation = relationWish.getValue().name();
            strRelationWishes.put(guildId, relation);
        }
        if (strRelationWishes.isEmpty()) return null;
        else return strRelationWishes;
    }

    public void remove(Guild guild) {
        String guildId = guild.getId();
        File rawFile = new File(getDataFolder() + "/" + guildId + ".yml");
        if (!rawFile.delete()) {
            System.out.println("Unable to delete " + guildId + ".yml");
        }
    }
}
