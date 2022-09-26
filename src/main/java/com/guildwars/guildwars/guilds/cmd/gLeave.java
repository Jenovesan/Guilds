package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;

public class gLeave extends gCommand{

    public gLeave() {
        // Name
        super("leave");

        // Reqs
        addReq(new InGuildReq());
    }

    @Override
    public void perform() throws CmdException {

        // Prepare

        // Req is not used here so specific message can be sent to player.
        // Leader cannot leave the guild. They must either give away leadership or disband the guild.
        if (gPlayer.getGuildRank() == GuildRank.LEADER) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.leave.is leader"));

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.player leave", guild.describe(gPlayer)), gPlayer);

        // Apply

        gPlayer.leftGuild();

        // Call event
        GPlayerGuildChangedEvent gPlayerGuildChangedEvent = new GPlayerGuildChangedEvent(gPlayer, guild, null, GPlayerGuildChangedEvent.Reason.LEAVE);
        gPlayerGuildChangedEvent.run();

        // Inform
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.leave.successfully left", gPlayer.describe(guild)));
    }
}
