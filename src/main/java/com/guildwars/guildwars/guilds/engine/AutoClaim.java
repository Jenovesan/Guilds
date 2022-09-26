package com.guildwars.guildwars.guilds.engine;


import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.*;
import com.guildwars.guildwars.guilds.utils.gUtil;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AutoClaim extends Engine {

    @EventHandler(priority = EventPriority.MONITOR)
    public void claimOnChunkUpdate(PlayerChunkUpdateEvent event) {
        if (event.isCancelled()) return;

        GPlayer player = event.getPlayer();

        if (!player.isAutoClaiming()) return;

        Chunk newChunk = event.getNewChunk();
        // Player moved into new chunk.
        // Try to claim.
        boolean claimed = player.tryClaim(Board.get().getChunkAt(newChunk));

        if (claimed) {
            // Inform player
            player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.claim.successfully claimed single chunk"));
        }
    }

    @EventHandler
    public void updatePlayersOnGuildRankChange(PlayerGuildRankChangedEvent event) {
        GPlayer player = event.getPlayer();
        if (!gUtil.checkPermission(player, GuildPermission.CLAIM)) {
            player.setAutoClaiming(false);
        }
    }

    @EventHandler
    public void removePlayerOnGuildChangeEvent(GPlayerGuildChangedEvent event) {
        GPlayer player = event.getGPlayer();
        if (player.isAutoClaiming()) player.setAutoClaiming(false);
    }

    @EventHandler
    public void updatePlayersOnGuildPermissionChange(GuildPermissionChangeEvent event) {
        if (event.getPermission() != GuildPermission.CLAIM) return;

        // Only need to update players if GuildRank for claiming was moved higher
        GuildRank newGuildRank = event.getNewGuildRank();
        if (newGuildRank.getLevel() < event.getOldGuildRank().getLevel()) return;

        for (GPlayer player : event.getGuild().getPlayers()) {
            GuildRank rank = player.getGuildRank();

            // Player's rank is below new guild rank
            if (rank.getLevel() < newGuildRank.getLevel()) {
                player.setAutoClaiming(false);
            }

        }
    }
}
