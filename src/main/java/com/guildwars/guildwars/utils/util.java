package com.guildwars.guildwars.utils;

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
}
