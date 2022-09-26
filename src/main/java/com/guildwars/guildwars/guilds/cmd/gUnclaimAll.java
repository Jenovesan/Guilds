package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.entity.GuildChunk;

public class gUnclaimAll extends gCommand{

    public gUnclaimAll() {
        // Name
        super("unclaimall");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.UNCLAIM_ALL));
    }

    @Override
    public void perform() {
        // Args
        int unclaims = 0;

        // Apply

        for (GuildChunk chunk : Board.get().getGuildClaims(guild)) {
            chunk.setWilderness();
            unclaims++;
        }

        // Inform

        // Inform guild
        if (unclaims > 0) guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.unclaimed all", guild.describe(gPlayer)));

        // Inform player
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.unclaimall.success"));
    }
}
