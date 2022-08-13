package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.event.GPlayerLeaveEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class MapAuto {
    private static HashMap<gPlayer, Chunk> players = new HashMap<>();

    private static HashMap<gPlayer, Chunk> getPlayers() {
        return players;
    }

    public static void addPlayer(gPlayer player) {
        getPlayers().put(player, player.getPlayer().getLocation().getChunk());
        player.sendSuccessMsg(Messages.getMsg("map auto.enabled"));
    }

    public static void removePlayer(gPlayer player) {
        getPlayers().remove(player);
        player.sendNotifyMsg(Messages.getMsg("map auto.disabled", player));
    }

    public static boolean isPlayer(gPlayer player) {
        return getPlayers().containsKey(player);
    }

    public static void perform() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<gPlayer, Chunk> entry : players.entrySet()) {
                    gPlayer player = entry.getKey();
                    Chunk oldChunk = entry.getValue();
                    Chunk newChunk = player.getPlayer().getLocation().getChunk();
                    // Player moved into new chunk
                    if (oldChunk != newChunk) {
                        player.sendMessage(Board.getMap(player));
                        // Update player chunk.
                        getPlayers().put(player, newChunk);
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 0, Config.get().getInt("autoclaim update time (ticks)"));

    }

    @EventHandler
    public void removePlayerOnLogout(GPlayerLeaveEvent event) {
        removePlayer(event.getGPlayer());
    }
}
