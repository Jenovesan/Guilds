package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
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
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.UNCLAIM, true)) {
            return;
        }

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.getMsg("commands.unclaim.cannot claim in world"));
            return;
        }

        Guild guild = player.getGuild();

        // Unclaim chunk(s)
        // Player is unclaiming in a radius
        if (args.length > 0) {
            try {
                int radius = Integer.parseInt(args[0]);

                // Check if radius is too big
                if (radius > Config.get().getInt("max unclaim radius (chunks)")) {
                    player.sendFailMsg(Messages.getMsg("commands.unclaim.radius too big", String.valueOf(radius)));
                    return;
                }

                int playerChunkX = Board.getChunkCord(player.getPlayer().getLocation().getChunk().getX());
                int playerChunkZ = Board.getChunkCord(player.getPlayer().getLocation().getChunk().getZ());
                int successfulUnclaims = 0;

                // Iterate through chunks in given radius and try to claim them
                for (int z = -radius + 1; z <= radius - 1; z++) {
                    for (int x = -radius + 1; x <= radius - 1; x++) {

                        // Get chunk
                        GuildChunk chunk = Board.getBoard()[playerChunkX + x][playerChunkZ + z];

                        // Check if chunk is owned by the player's guild
                        if (chunk.getGuild() != guild) {
                            continue;
                        }

                        // Unclaim chunk
                        guild.unclaim(player, chunk);

                        // Track successful claim
                        successfulUnclaims++;
                    }
                }

                // Inform plauer
                player.sendSuccessMsg(Messages.getMsg("commands.unclaim.successfully unclaimed multiple chunks", String.valueOf(successfulUnclaims)));

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.unclaim.invalid radius", args[0]));
            }
        }
        // Player is unclaiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            // Check if chunk is owned by the player's guild
            if (chunk.getGuild() != guild) {
                player.sendFailMsg(Messages.getMsg("commands.unclaim.chunk not owned by guild"));
                return;
            }

            // Unclaim chunk
            guild.unclaim(player, chunk);

            // Inform player
            player.sendSuccessMsg(Messages.getMsg("commands.unclaim.successfully unclaimed single chunk"));
        }
    }
}
