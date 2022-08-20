package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.event.GPlayerLeaveEvent;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashSet;

public class MapAuto {
    private static HashSet<gPlayer> players = new HashSet<>();

    private static HashSet<gPlayer> getPlayers() {
        return players;
    }

    public static void addPlayer(gPlayer player) {
        getPlayers().add(player);
        player.sendSuccessMsg(Messages.getMsg("map auto.enabled"));
    }

    public static void removePlayer(gPlayer player) {
        getPlayers().remove(player);
        player.sendNotifyMsg(Messages.getMsg("map auto.disabled", player));
    }

    public static boolean isPlayer(gPlayer player) {
        return getPlayers().contains(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void sendMapOnPlayerChunkUpdate(PlayerChunkUpdateEvent event) {
        if (event.isCancelled()) return;

        gPlayer player = event.getPlayer();

        player.sendMessage(Board.getMap(player));
    }

    @EventHandler
    public void removePlayerOnLogout(GPlayerLeaveEvent event) {
        removePlayer(event.getGPlayer());
    }
}
