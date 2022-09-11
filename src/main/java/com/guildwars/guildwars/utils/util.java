package com.guildwars.guildwars.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

    public static ChatColor getRandomColor() {
        ThreadLocalRandom gen = ThreadLocalRandom.current();
        int randR = gen.nextInt(0, 256);
        int randG = gen.nextInt(0, 256);
        int randB = gen.nextInt(0, 256);

        return ChatColor.of(new Color(randR, randG, randB));
    }

    public static long getTimeLater(int minutesLater) {
        return System.currentTimeMillis() + (minutesLater * 60000L);
    }

    public static Object getSingleKeyByValue(HashMap<?, ?> hashMap, Object value) {
        for (Map.Entry<?, ?> entry : hashMap.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
