package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.entity.Invitation;
import com.guildwars.guildwars.guilds.cmd.arg.GuildArg;
import com.guildwars.guildwars.guilds.cmd.req.NotInGuildReq;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;

public class gJoin extends gCommand{

    public gJoin() {
        // Name
        super("join");

        // Reqs
        addReq(new NotInGuildReq());

        // Args
        addArg(new GuildArg(true));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        Guild guildToJoin = readNextArg();

        // Prepare

        Invitation invite = guildToJoin.getInvite(gPlayer);

        // Can only join the guild if gPlayer is invited
        if (invite == null) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.join.not invited", gPlayer.describe(guildToJoin)));

        // Cannot join the guild if the guild is full
        if (guildToJoin.isFull()) {
            // Send guild announcement
            guildToJoin.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.guild was full", guildToJoin.describe(gPlayer)));

            throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.join.guild is full", gPlayer.describe(guildToJoin)));
        }

        // Apply

        // Update gPlayer
        gPlayer.joinedNewGuild(guildToJoin);

        // Call event
        GPlayerGuildChangedEvent gPlayerGuildChangedEvent = new GPlayerGuildChangedEvent(gPlayer, null, guildToJoin, GPlayerGuildChangedEvent.Reason.JOIN);
        gPlayerGuildChangedEvent.run();

        // Remove invite
        guildToJoin.removeInvite(invite);

        // Inform

        // Send Guild Announcement
        guildToJoin.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.player join", guildToJoin.describe(gPlayer)), gPlayer);

        // Inform joiner
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.join.success", gPlayer.describe(guildToJoin)));
    }
}
