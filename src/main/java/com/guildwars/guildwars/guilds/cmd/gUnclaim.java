package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;

public class gUnclaim extends gCommand{

    public gUnclaim() {
        super("unclaim");
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

        // Unclaim chunk(s)
        // Player is unclaiming in a radius
        if (args.length > 0) {
            try {
                int radius = Integer.parseInt(args[0]);

                // Check if radius is too big
                if (radius > Config.get().getInt("max unclaim radius (chunks)")) {
                    player.sendFailMsg(Messages.getMsg("commands.unclaim.radius too big", radius));
                    return;
                }

                if (radius < 0) {
                    player.sendFailMsg(Messages.getMsg("commands.unclaim.invalid radius", args[0]));
                    return;
                }

                GuildChunk[] guildChunksToUnclaim = Board.getNearbyChunks(player.getPlayer().getLocation(), radius);

                int successfulUnclaims = player.tryUnclaim(guildChunksToUnclaim);

                // Inform
                if (successfulUnclaims > 0) {
                    // Inform guild
                    player.getGuild().sendAnnouncement(Messages.getMsg("guild announcements.unclaimed many land", player, successfulUnclaims));
                    // Inform player
                    player.sendSuccessMsg(Messages.getMsg("commands.claim.successfully unclaimed multiple chunks", successfulUnclaims));
                }
                // Player did not unclaim any chunks
                else {
                    player.sendNotifyMsg(Messages.getMsg("commands.claim.successfully unclaimed multiple chunks", successfulUnclaims));
                }

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.getMsg("commands.unclaim.invalid radius", args[0]));
            }
        }
        // Player is unclaiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            boolean unclaimed = player.tryUnclaim(chunk);

            if (unclaimed) {
                // Inform player
                player.sendSuccessMsg(Messages.getMsg("commands.unclaim.successfully unclaimed single chunk"));
            }
        }
    }
}
