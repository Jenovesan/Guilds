package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.utils.util;

public class ConfigFile extends GuildWarsFile<Object> {

    public ConfigFile(String folder) {
        super("config", folder);
    }

    public int getInt(String string) {
        return (int) get(string);
    }

    public double getDouble(String string) {
        return (double) get(string);
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

    private Object get(String string) {
        Object obj = messagesCache.get(string);
        if (obj == null) {
            obj = getConfiguration().get(string);
            messagesCache.put(string, obj);
        }
        return obj;
    }
}
