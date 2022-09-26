package com.guildwars.guildwars.utils;

import com.guildwars.guildwars.GuildWarsFile;

import java.util.HashMap;
import java.util.Map;

public class fUtil {
    // Only supports bukkit-supported objects
    @SuppressWarnings("unchecked")
    public static <V> HashMap<String, V> getHashMapFromFile(GuildWarsFile<?> file, String path) {
        Map<String, Object> rawMap = file.getConfiguration().getConfigurationSection(path).getValues(false);
        HashMap<String, V> ret = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), (V) entry.getValue());
        }
        return ret;
    }
}
