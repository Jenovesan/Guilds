package com.guildwars.guildwars.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class pUtil {

    // Player Util

    public static void sendSuccessMsg(Player player, String msg) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        player.sendMessage(msg);
    }

    public static void sendFailMsg(Player player, String msg) {
        player.playSound(player, Sound.ENTITY_GHAST_SCREAM, 10, 1);
        player.sendMessage(msg);
    }

    public static void sendNotifyMsg(Player player, String msg) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
        player.sendMessage(msg);
    }
}
