package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GPlayerLeaveEvent;
import com.guildwars.guildwars.guilds.event.GuildPermissionChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class AutoClaim implements Listener {
    private static HashMap<gPlayer, Chunk> players = new HashMap<>();

    private static HashMap<gPlayer, Chunk> getPlayers() {
        return players;
    }

    public static void addPlayer(gPlayer player) {
        getPlayers().put(player, null);
        player.sendSuccessMsg(Messages.getMsg("autoclaiming.enabled", player, null, null));
    }

    public static void removePlayer(gPlayer player) {
        if (getPlayers().containsKey(player)) {
            player.sendNotifyMsg(Messages.getMsg("autoclaiming.disabled", player, null, null));
            getPlayers().remove(player);
        }
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
                        Guild guild = player.getGuild();

                        // Checks
                        if (!guild.canClaim()) {
                            player.sendFailMsg(Messages.getMsg("commands.claim.not enough power", player, null, null));
                            continue;
                        }

                        GuildChunk guildChunk = Board.getChunk(newChunk);

                        if (!guildChunk.isClaimable()) {
                            player.sendFailMsg(Messages.getMsg("commands.claim.not overclaimable", player, null, guildChunk.getGuild().getName()));
                            continue;
                        }

                        // Claim chunk
                        guild.claim(player, guildChunk);

                        // Inform player
                        player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed single chunk", player, null, String.valueOf(player.getPlayer().getLocation())));
                    }
                    // Update player chunk.
                    getPlayers().put(player, newChunk);
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 0, Config.get().getInt("autoclaim update time (ticks)"));
    }

    @EventHandler
    public void updatePlayersOnGuildRankChange(PlayerGuildRankChangeEvent event) {
        gPlayer player = event.getPlayer();
        if (!gUtil.checkPermission(player, GuildPermission.CLAIM, false)) {
            removePlayer(player);
        }
    }

    @EventHandler
    public void removePlayerOnGuildChangeEvent(PlayerGuildChangeEvent event) {
        removePlayer(event.getPlayer());
    }

    @EventHandler
    public void updatePlayersOnGuildPermissionChange(GuildPermissionChangeEvent event) {
        if (event.getPermission() != GuildPermission.CLAIM) {
            return;
        }

        // Only need to update players in GuildRank for claiming was moved higher
        if (event.getNewGuildRank().level < event.getOldGuildRank().level) {
            return;
        }

        Guild guild = event.getGuild();
        for (gPlayer player : getPlayers().keySet()) {
            if (player.getGuild() != guild) {
                continue;
            }
            // Player does not have permission to claim anymore
            if (!gUtil.checkPermission(player, GuildPermission.CLAIM, false)) {
                removePlayer(player);
            }
        }
    }

    @EventHandler
    public void removePlayerOnLogout(GPlayerLeaveEvent event) {
        removePlayer(event.getGPlayer());
    }

}
