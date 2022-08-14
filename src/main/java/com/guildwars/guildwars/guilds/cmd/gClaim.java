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

        Guild guild = player.getGuild();

        // Claim chunk(s)
        // Player is claiming a radius
        if (args.length > 0) {
            try {
                int radius = Integer.parseInt(args[0]);

                // Check if Guild will have enough power
                if ((radius + 1) * (radius + 1) > guild.getExcessPower()) {
                    player.sendFailMsg(Messages.getMsg("commands.claim.will not have enough power"));
                    return;
                }

                int playerChunkX = Board.getChunkCord(player.getPlayer().getLocation().getChunk().getX());
                int playerChunkZ = Board.getChunkCord(player.getPlayer().getLocation().getChunk().getZ());
                int successfulClaims = 0;

                // Iterate through chunks in given radius and try to claim them
                for (int z = -radius + 1; z <= radius - 1; z++) {
                    for (int x = -radius + 1; x <= radius - 1; x++) {
                        // Get the chunk
                        GuildChunk chunk = Board.getBoard()[playerChunkX + x][playerChunkZ + z];

                        // Try to claim
                        boolean claimed = player.tryClaim(chunk);

                        if (claimed) {
                            // Track successful claim
                            successfulClaims++;
                        }
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
