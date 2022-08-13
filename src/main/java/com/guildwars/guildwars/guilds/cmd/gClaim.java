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
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.CHAT, true)) {
            return;
        }

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.getMsg("commands.claim.cannot claim in world", player, null, String.join(" ", args)));
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
                    player.sendFailMsg(Messages.getMsg("commands.claim.will not have enough power", player, null, String.valueOf((radius + 1) * (radius + 1))));
                    return;
                }

                int playerChunkX = player.getPlayer().getLocation().getChunk().getX();
                int playerChunkZ = player.getPlayer().getLocation().getChunk().getZ();
                int successfulClaims = 0;

                // Iterate through chunks in given radius and try to claim them
                for (int z = playerChunkZ - radius + 1; z <= playerChunkZ + radius - 1; z++) {
                    for (int x = playerChunkX - radius + 1; x <= playerChunkX + radius - 1; x++) {

                        // Check if guild can claim
                        if (!guild.canClaim()) {
                            player.sendFailMsg(Messages.getMsg("commands.claim.not enough power", player, null, String.join(" ", args)));
                            return;
                        }

                        // Get the chunk
                        GuildChunk chunk = Board.getBoard()[Board.getChunkCord(x)][Board.getChunkCord(z)];

                        // Check if it is claimable
                        if (!chunk.isClaimable()) {
                            player.sendFailMsg(Messages.getMsg("commands.claim.not overclaimable", player, null, chunk.getGuild().getName()));
                            continue;
                        }

                        // Claim chunk
                        guild.claim(player, chunk);

                        // Track successful claim
                        successfulClaims++;
                    }
                }

                // Inform plauer
                player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed multiple chunks", player, null, String.valueOf(successfulClaims)));

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.claim.invalid radius", player, null, String.join(" ", args)));
            }
        }
        // Player is claiming a single chunk
        else {

            // Check if guild can claim
            if (!guild.canClaim()) {
                player.sendFailMsg(Messages.getMsg("commands.claim.not enough power", player, null, String.join(" ", args)));
                return;
            }

            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            // Check if it's claimable
            if (!chunk.isClaimable()) {
                player.sendFailMsg(Messages.getMsg("commands.claim.not overclaimable", player, null, chunk.getGuild().getName()));
                return;
            }

            // Claim chunk
            guild.claim(player, chunk);

            // Inform plauer
            player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully claimed single chunk", player, null, String.valueOf(player.getPlayer().getLocation())));
        }
    }
}
