package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangedEvent;
import com.guildwars.guildwars.utils.util;

public class gPromote extends gCommand{

    public gPromote() {
        // Name
        super("promote");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.PROMOTE));

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        GPlayer promotee = readNextArg();
        GuildRank promoteeGuildRank = promotee.getGuildRank();

        // Prepare

        // Can only promote player if they are in your guild
        if (promotee.getGuild() != guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player not in sender guild", gPlayer.describe(promotee)));

        // Can only promote a player if you are 2 GuildRanks higher than them.
        // Makes is so players cannot promote others to the same rank as themselves
        GuildRank rankToPromote = GuildRank.getGuildRankByLevel(Math.min(promotee.getGuildRank().getLevel() + 2, GuildRank.LEADER.getLevel()));
        if (gPlayer.getGuildRank().getLevel() < rankToPromote.getLevel()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.promote.rank not high enough", util.formatEnum(rankToPromote), gPlayer.describe(promotee)));

        if (promoteeGuildRank == GuildRank.COLEADER) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.promote.tried to make leader"));

        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(promoteeGuildRank.getLevel() + 1);

        // Apply

        promotee.setGuildRank(newGuildRank);

        // Call Event
        PlayerGuildRankChangedEvent playerGuildRankChangeEvent = new PlayerGuildRankChangedEvent(promotee, newGuildRank);
        playerGuildRankChangeEvent.run();

        // Inform

        // Inform promotee
        promotee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.promote.success to promotee", util.formatEnum(newGuildRank)));

        // Inform promoter
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.promote.success", gPlayer.describe(promotee), util.formatEnum(newGuildRank)));
    }
}
