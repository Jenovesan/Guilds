package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.utils.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config extends GuildWarsFile<Object> {

    private static HashMap<Plugin, Config> configFiles = new HashMap<>();

    public static Config get(Plugin plugin) {
        return configFiles.get(plugin);
    }

    public Config(Plugin plugin) {
        super("config", plugin);
        configFiles.put(plugin, this);
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public float getFloat(String path) {
        return ((Double) get(path)).floatValue();
    }

    public String getString(String path) {
        return util.translateColors((String) get(path));
    }

    public Character getChar(String path) {
        return (Character) get(path);
    }

    public GuildRank getGuildRank(String path) {
        return GuildRank.valueOf((String) get(path));
    }

    public Long getLong(String path) {
        return Long.valueOf((Integer) get(path));
    }

    public Boolean getBoolean(String path) {
        return (Boolean) get(path);
    }

    public Map<String, Object> getMap(String path) {
        return getConfiguration().getConfigurationSection(path).getValues(false);
    }

    public List<Character> getCharacterList(String path) {
        List<Character> characterList = new ArrayList<>();
        for (String stringChar : getStringList(path)) {
            characterList.add(stringChar.charAt(0));
        }
        return characterList;
    }

    public List<String> getStringList(String path) {
        return (List<String>) get(path);
    }

    private Object get(String path) {
        Object obj = messagesCache.get(path);
        if (obj == null) {
            obj = getConfiguration().get(path);
            messagesCache.put(path, obj);
        }
        return obj;
    }
}
