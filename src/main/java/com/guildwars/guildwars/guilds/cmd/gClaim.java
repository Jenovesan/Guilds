package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
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
                int radius = Integer.parseInt(args[0]) - 1;

                if (radius < 1) {
                    player.sendFailMsg(Messages.getMsg("commands.claim.invalid radius", args[0]));
                    return;
                }

                // Check if Guild will have enough power
                if (((radius * 2) + 1) * ((radius * 2) + 1) > player.getGuild().getExcessPower()) {
                    player.sendFailMsg(Messages.getMsg("commands.claim.will not have enough power"));
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
                player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed multiple chunks", successfulClaims));

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.claim.invalid radius", args[0]));
            }
        }
        // Player is claiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            // Try to claim
            boolean claimed = player.tryClaim(chunk);

            if (claimed) {
                // Inform plauer
                player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed single chunk"));
            }
        }
    }
}
