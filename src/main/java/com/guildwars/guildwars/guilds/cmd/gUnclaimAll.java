package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;

import java.util.HashSet;

public class gUnclaimAll extends gCommand{

    public gUnclaimAll() {
        super("unclaimall");
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.UNCLAIM_ALL, true)) {
            return;
        }

        // Unclaim all
        Guild guild = player.getGuild();

        // Update Board
        HashSet<int[]> claimLocations = guild.getClaimLocations();
        GuildChunk[] claims = new GuildChunk[claimLocations.size()];
        int i = 0;
        for (int[] boardLocation : claimLocations) {
            claims[i] = Board.getBoard()[boardLocation[0]][boardLocation[1]];
            i++;
        }
        int unclaims = player.tryUnclaim(claims);

        if (unclaims > 0) {
            // Send Guild announcement
            guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.unclaimed all", player));
        }

        // Inform player
        player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.unclaimall.successfully unclaimed all"));
    }
}
