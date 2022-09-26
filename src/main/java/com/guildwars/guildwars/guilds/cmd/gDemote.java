package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangedEvent;
import com.guildwars.guildwars.utils.util;

public class gDemote extends gCommand {

    public gDemote() {
        // Name
        super("demote");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.PROMOTE));

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        GPlayer demotee = readNextArg();
        GuildRank demoterGuildRank = gPlayer.getGuildRank();
        GuildRank demoteeGuildRank = demotee.getGuildRank();

        // Prepare

        // Player cannot only demote demotee if they are a higher rank than demotee
        if (GuildRank.higherByAmount(demoterGuildRank, demoteeGuildRank) < 1) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.demote.rank not high enough", gPlayer.describe(demotee)));

        // Player cannot demote someone to a GuildRank lower than Recruit
        if (demoteeGuildRank.getLevel() == 1) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.demote.cannot demote any further", gPlayer.describe(demotee)));

        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(demoteeGuildRank.getLevel() - 1);

        // Apply

        demotee.setGuildRank(newGuildRank);

        // Call Event
        PlayerGuildRankChangedEvent playerGuildRankChangedEvent = new PlayerGuildRankChangedEvent(demotee, newGuildRank);
        playerGuildRankChangedEvent.run();

        // Inform

        // Inform demoter
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.demote.success", gPlayer.describe(demotee), util.formatEnum(newGuildRank)));

        // Inform demotee
        demotee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.demote.success to demotee", util.formatEnum(newGuildRank)));

    }
}
