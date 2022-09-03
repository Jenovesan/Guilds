package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.ObjectDataManager;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        guildData.put("players", guild.getPlayers().stream().map(gPlayer::getUUID).collect(Collectors.toList()));
        guildData.put("permissions", util.hashMapToHashMapString(guild.getPermissions()));
        guildData.put("enemies", guild.getEnemies().stream().map(Guild::getId).collect(Collectors.toList()));
        List<String> claimLocations = new ArrayList<>();
        guild.getClaimLocations().forEach(claim -> claimLocations.add(claim[0] + ":" + claim[1]));
        guildData.put("claimLocations", claimLocations);
        if (guild.getRaidedBy() != null) guildData.put("raidedBy", guild.getRaidedBy().getId());
        guildData.put("raidEndTime", guild.getRaidEndTime());

        super.saveRaw(guild.getId(), guildData);
    }

    public void remove(Guild guild) {
        String guildId = guild.getId();
        File rawFile = new File(getDataFolder() + "/" + guildId + ".yml");
        if (!rawFile.delete()) {
            System.out.println("Unable to delete " + guildId + ".yml");
        }
    }
}
