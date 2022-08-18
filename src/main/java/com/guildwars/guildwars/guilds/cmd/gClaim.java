package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;

public class gClaim extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.power.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.power.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.CHAT, true)) {
            return;
        }

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.getMsg("commands.claim.cannot claim in world"));
            return;
        }

        // Claim chunk(s)
        // Player is claiming a radius
        if (args.length > 0) {
            try {
                // Subtracting by 1 because that is how the original factions-plugin did it.
                // Want to keep it familiar for players.
                // Also makes it so g claim 1 claims just 1 chunk.
                int radius = Integer.parseInt(args[0]) - 1;

                // Check if radius is too big
                if (radius > Config.get().getInt("max claim radius (chunks)")) {
                    player.sendFailMsg(Messages.getMsg("commands.claim.radius too big", args[0]));
                    return;
                }

                if (radius < 0) {
                    player.sendFailMsg(Messages.getMsg("commands.claim.invalid radius", args[0]));
                    return;
                }

                int successfulClaims = 0;

                GuildChunk[] guildChunksToClaim = Board.getNearbyChunks(player.getPlayer().getLocation(), radius);

                for (GuildChunk chunk : guildChunksToClaim) {
                    // Try to claim
                    boolean claimed = player.tryClaim(chunk);

                    if (claimed) {
                        // Track successful claim
                        successfulClaims++;
                    }
                }

                // Inform plauer
                if (successfulClaims > 0) {
                    player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed multiple chunks", successfulClaims));
                }
                // Player did not claim any chunks
                else {
                    player.sendNotifyMsg(Messages.getMsg("commands.claim.successfully claimed multiple chunks", successfulClaims));
                }

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.claim.invalid radius", args[0]));
            }
        }
        // Player is claiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            // Claiming in outlands
            if (chunk == null) {
                player.sendFailMsg(Messages.getMsg("claiming.claiming in outlands"));
                return;
            }

            // Try to claim
            boolean claimed = player.tryClaim(chunk);

            if (claimed) {
                // Inform plauer
                player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed single chunk"));
            }
        }
    }
}
