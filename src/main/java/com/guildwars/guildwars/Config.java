package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.utils.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config extends GuildWarsFile<Object> {

    private static HashMap<Plugin, Config> configFiles = new HashMap<>();

    public static Config get(Plugin plugin) {
        return configFiles.get(plugin);
    }

    public Config(Plugin plugin) {
        super("config", plugin);
        configFiles.put(plugin, this);
    }

    public int getInt(String string) {
        return (int) get(string);
    }

    public float getFloat(String string) {
        return ((Double) get(string)).floatValue();
    }

    public String getString(String string) {
        return util.translateColors((String) get(string));
    }

    public Character getChar(String string) {
        return (Character) get(string);
    }

    public GuildRank getGuildRank(String string) {
        return GuildRank.valueOf((String) get(string));
    }

    public Long getLong(String string) {
        return Long.valueOf((Integer) get(string));
    }

    public List<Character> getCharacterList(String string) {
        List<Character> characterList = new ArrayList<>();
        for (String stringChar : getStringList(string)) {
            characterList.add(stringChar.charAt(0));
        }
        return characterList;
    }

    public List<String> getStringList(String string) {
        return (List<String>) get(string);
    }

    private Object get(String string) {
        Object obj = messagesCache.get(string);
        if (obj == null) {
            obj = getConfiguration().get(string);
            messagesCache.put(string, obj);
        }
        return obj;
    }
}
