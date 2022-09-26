package com.guildwars.guildwars;

import com.guildwars.guildwars.utils.util;

import java.util.HashMap;
import java.util.List;

public class Messages extends GuildWarsFile<String> {

    private static HashMap<Plugin, Messages> messageFiles = new HashMap<>();

    public static Messages get(Plugin plugin) {
        return messageFiles.get(plugin);
    }

    public Messages(Plugin plugin) {
        super("messages", plugin);
        messageFiles.put(plugin, this);
    }


    public String get(String path, Object... objects) {

        String string = messagesCache.get(path);

        // First time string is retrieved from configuration
        if (string == null) {
            string = getConfiguration().getString(path);
            messagesCache.put(path, string);
        }

        if (objects.length == 0) {
            return util.translateColors(string);
        }
        else {
            return util.translateColors(format(string, objects));
        }
    }

    public String[] getArr(String path) {
        List<String> stringList = getConfiguration().getStringList(path);
        String[] stringArray = new String[stringList.size()];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = util.translateColors(stringList.get(i));
        }
        return stringArray;
    }

    private String format(String string, Object... objects) {
        for (Object object : objects) {
            if (object instanceof String) {
                string = string.replaceFirst("%S%", (String) object);
            }
            else if (object instanceof Integer) {
                string = string.replaceFirst("%I%", String.valueOf(object));
            }
            else if (object instanceof Character) {
                string = string.replaceFirst("%C%", String.valueOf(object));
            }
        }
        return string;
    }
}
