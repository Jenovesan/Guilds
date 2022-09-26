package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildLeaderReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangedEvent;

public class gLeader extends gCommand{

    public gLeader() {
        // Name
        super("leader");

        // Aliases
        addAlias("owner");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildLeaderReq());

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        GPlayer newLeader = readNextArg();

        // Prepare

        // Cannot make yourself leader
        if (gPlayer == newLeader) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player is sender"));

        // Can only make someone in your guild leader
        if (newLeader.getGuild() != guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player not in your guild", gPlayer.describe(newLeader)));

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.gave leadership", guild.describe(gPlayer), guild.describe(newLeader)));

        // Apply

        newLeader.setGuildRank(GuildRank.LEADER);
        gPlayer.setGuildRank(GuildRank.COLEADER);

        // Call Events
        PlayerGuildRankChangedEvent newLeaderGuildRankChangeEvent = new PlayerGuildRankChangedEvent(newLeader, GuildRank.LEADER);
        newLeaderGuildRankChangeEvent.run();

        PlayerGuildRankChangedEvent oldLeaderGuildRankChangeEvent = new PlayerGuildRankChangedEvent(gPlayer, GuildRank.COLEADER);
        oldLeaderGuildRankChangeEvent.run();

        // Inform

        // Inform newLeader
        newLeader.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.leader.success to new leader", newLeader.describe(guild)));

        // Inform oldLeader
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.leader.success", gPlayer.describe(newLeader), gPlayer.describe(guild)));
    }
}
