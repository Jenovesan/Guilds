package com.guildwars.guildwars.guilds.engine;


import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.*;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;

public class AutoClaim implements Listener {
    private static HashSet<gPlayer> players = new HashSet<>();

    private static HashSet<gPlayer> getPlayers() {
        return players;
    }

    public static void addPlayer(gPlayer player) {
        getPlayers().add(player);
        player.sendSuccessMsg(Messages.getMsg("autoclaiming.enabled"));
    }

    public static void removePlayer(gPlayer player) {
        if (getPlayers().contains(player)) {
            player.sendNotifyMsg(Messages.getMsg("autoclaiming.disabled"));
            getPlayers().remove(player);
        }
    }

    public static boolean isPlayer(gPlayer player) {
        return getPlayers().contains(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void claimOnChunkUpdate(PlayerChunkUpdateEvent event) {
        if (event.isCancelled()) return;

        gPlayer player = event.getPlayer();
        Chunk newChunk = event.getNewChunk();
        // Player moved into new chunk.
        // Try to claim.
        boolean claimed = player.tryClaim(Board.getChunk(newChunk));

        if (claimed) {
            // Inform player
            player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed single chunk"));
        }
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
        for (gPlayer player : getPlayers()) {
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
