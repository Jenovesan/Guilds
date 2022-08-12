package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;

public class gUnclaim extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.unclaim.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.unclaim.usage");
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

        if (!gUtil.checkPermission(player, GuildPermission.UNCLAIM)) {
            return;
        }

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.getMsg("commands.unclaim.cannot claim in world", player, null, String.join(" ", args)));
            return;
        }

        Guild guild = player.getGuild();

        // Unclaim chunk(s)
        // Player is unclaiming in a radius
        if (args.length > 0) {
            try {
                int radius = Integer.parseInt(args[0]);
                int playerChunkX = player.getPlayer().getLocation().getChunk().getX();
                int playerChunkZ = player.getPlayer().getLocation().getChunk().getZ();
                int successfulUnclaims = 0;

                // Iterate through chunks in given radius and try to claim them
                for (int z = playerChunkZ + radius - 1; z >= playerChunkZ - radius + 1; z--) {
                    for (int x = playerChunkX + radius - 1; x >= playerChunkX - radius + 1; x--) {

                        // Get chunk
                        GuildChunk chunk = Board.getBoard()[Board.getChunkCord(x)][Board.getChunkCord(z)];

                        // Check if chunk is owned by the player's guild
                        if (chunk.getGuild() != guild) {
                            player.sendMessage(Messages.getMsg("commands.unclaim.chunk not owned by guild", player, null, String.join(" ", args)));
                            continue;
                        }

                        // Unclaim chunk
                        guild.unclaim(player, chunk);

                        // Track successful claim
                        successfulUnclaims++;
                    }
                }

                // Inform plauer
                player.sendSuccessMsg(Messages.getMsg("commands.unclaim.successfully unclaimed multiple chunks", player, null, String.valueOf(successfulUnclaims)));

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.unclaim.invalid radius", player, null, String.join(" ", args)));
            }
        }
        // Player is unclaiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            // Check if chunk is owned by the player's guild
            if (chunk.getGuild() != guild) {
                player.sendFailMsg(Messages.getMsg("commands.unclaim.chunk not owned by guild", player, null, String.join(" ", args)));
                return;
            }

            // Unclaim chunk
            guild.unclaim(player, chunk);

            // Inform player
            player.sendSuccessMsg(Messages.getMsg("commands.unclaim.successfully unclaimed single chunk", player, null, String.valueOf(player.getPlayer().getLocation())));
        }
    }
}
