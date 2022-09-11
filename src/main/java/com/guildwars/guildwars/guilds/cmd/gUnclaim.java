package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;

public class gUnclaim extends gCommand{

    public gUnclaim() {
        super("unclaim");
        mustBeInGuild(true);
        setMinPermission(GuildPermission.UNCLAIM);
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.cannot claim in world"));
            return;
        }

        // Unclaim chunk(s)
        // Player is unclaiming in a radius
        if (args.length > 0) {
            try {
                int radius = Integer.parseInt(args[0]);

                // Check if radius is too big
                if (radius > Config.get(Plugin.GUILDS).getInt("max unclaim radius (chunks)")) {
                    player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.radius too big", radius));
                    return;
                }

                if (radius < 0) {
                    player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.invalid radius", args[0]));
                    return;
                }

                GuildChunk[] guildChunksToUnclaim = Board.getNearbyChunks(player.getPlayer().getLocation(), radius);

                int successfulUnclaims = player.tryUnclaim(guildChunksToUnclaim);

                // Inform
                if (successfulUnclaims > 0) {
                    // Inform guild
                    player.getGuild().sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.unclaimed many land", player, successfulUnclaims));
                    // Inform player
                    player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.successfully unclaimed multiple chunks", successfulUnclaims));
                }
                // Player did not unclaim any chunks
                else {
                    player.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.successfully unclaimed multiple chunks", successfulUnclaims));
                }

            } catch (NumberFormatException e) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.invalid radius", args[0]));
            }
        }
        // Player is unclaiming a single chunk
        else {
            // Get chunk
            GuildChunk chunk = Board.getGuildChunkAt(player.getPlayer().getLocation());

            boolean unclaimed = player.tryUnclaim(chunk);

            if (unclaimed) {

                // Inform player
                player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.successfully unclaimed single chunk"));
            }
        }
    }
}
