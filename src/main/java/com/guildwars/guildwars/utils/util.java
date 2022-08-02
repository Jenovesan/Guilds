package com.guildwars.guildwars.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class util {

    public static HashMap<String, String> hashMapToHashMapString(HashMap<?,?> map) {
        HashMap<String, String> newMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            newMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return newMap;
    }

    public static boolean containsIgnoreCase(Collection<String> strings, String searchFor) {
        for (String string : strings) {
            if (string.equalsIgnoreCase(searchFor)) {
                return true;
            }
        }
        return false;
    }

    public static String formatEnum(Enum<?> enumToFormat) {
        return enumToFormat.name().substring(0, 1).toUpperCase() + enumToFormat.name().substring(1).toLowerCase();
    }
}
