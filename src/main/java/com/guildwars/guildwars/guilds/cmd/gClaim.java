package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.IntArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.cmd.req.InMainWorldReq;

import java.util.Arrays;

public class gClaim extends gCommand{

    public gClaim() {
        // Name
        super("claim", "unclaim");

        // Aliases
        addAlias("uclaim");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.CLAIM));
        addReq(new InMainWorldReq());

        // Args
        addArg(new IntArg(false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        boolean claim = getCmd().equalsIgnoreCase("claim");
        int radius = readNextArg(0);

        // Prepare

        // Check if radius is valid
        if (radius < 0) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.claim.invalid radius", String.valueOf(radius)));

        // Check if radius is too big
        if (radius > Config.get(Plugin.GUILDS).getInt("max claim radius (chunks)")) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.claim.radius too big", radius));

        // Apply

        int successes = gPlayer.tryClaim(getSquareChunks(radius), claim, radius == 0);

        // Inform

        // Inform Guild
        // Player claimed single chunk
        if (successes == 1) {
            // Inform guild
            if (claim) guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.claimed one", guild.describe(gPlayer), successes));
            else guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.unclaimed one", guild.describe(gPlayer), successes));

            // Inform player
            gPlayer.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.claim.success one", successes));
        }
        // Player gained multiple chunks
        else if (successes > 1) {
            // Inform guild
            if (claim) guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.claimed many", guild.describe(gPlayer), successes));
            else guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.unclaimed many", guild.describe(gPlayer), successes));

            // Inform player
            gPlayer.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.claim.success many", successes));
        }
    }

    private GuildChunk[] getSquareChunks(int radius) {
        // Uses a reverse-spiral matrix algorithm so claims will be connected while claiming in a radius

        // Get starting position
        int x = player.getLocation().getChunk().getX();
        int z = player.getLocation().getChunk().getZ();

        // Create variables
        int diameter = (radius * 2) + 1;
        int size = diameter * diameter;
        GuildChunk[] nearbyChunks = new GuildChunk[size];

        // Create variables for reverse-spiral
        int len = 0;
        int d = 0;
        int[] directions = new int[]{0, 1, 0, -1, 0};

        // Reverse-spiral
        for (int i = 0; i < size;) {
            if (d == 0 || d == 2) {
                len++;
            }
            for (int k = 0; k < len; k++) {
                nearbyChunks[i++] = Board.get().getGuildChunkAt(x, z);
                x += directions[d];
                z += directions[d + 1];
            }

            d = ++d % 4;
        }
        return nearbyChunks;
    }
}
