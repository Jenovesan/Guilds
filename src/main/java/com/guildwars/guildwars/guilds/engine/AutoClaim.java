package com.guildwars.guildwars.guilds.engine;


import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.*;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Map;

public class AutoClaim extends Engine {

    @EventHandler(priority = EventPriority.MONITOR)
    public void claimOnChunkUpdate(PlayerChunkUpdateEvent event) {
        if (event.isCancelled()) return;

        gPlayer player = event.getPlayer();

        if (!player.isAutoClaiming()) return;

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
            player.setAutoClaiming(false);
            player.sendNotifyMsg(Messages.getMsg("autoclaiming.disabled"));
        }
    }

    @EventHandler
    public void removePlayerOnGuildChangeEvent(PlayerGuildChangeEvent event) {
        gPlayer player = event.getPlayer();
        if (player.isAutoClaiming()) {
            player.sendNotifyMsg(Messages.getMsg("autoclaiming.disabled"));
        }
        player.setAutoClaiming(false);
    }

    @EventHandler
    public void updatePlayersOnGuildPermissionChange(GuildPermissionChangeEvent event) {
        if (event.getPermission() != GuildPermission.CLAIM) return;

        // Only need to update players if GuildRank for claiming was moved higher
        GuildRank newGuildRank = event.getNewGuildRank();
        if (newGuildRank.level < event.getOldGuildRank().level) return;

        Guild guild = event.getGuild();

        for (Map.Entry<gPlayer, GuildRank> playerRank : guild.getPlayers().entrySet()) {
            GuildRank rank = playerRank.getValue();

            // Player's rank is below new guild rank
            if (rank.level < newGuildRank.level) {
                gPlayer player = playerRank.getKey();
                player.setAutoClaiming(false);
                player.sendNotifyMsg(Messages.getMsg("autoclaiming.disabled"));
            }

        }
    }
}
