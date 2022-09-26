package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;
import com.guildwars.guildwars.utils.util;

public class gKick extends gCommand{

    public gKick() {
        // Name
        super("kick");

        // Aliases
        addAlias("remove");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.INVITE));

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {

        // Args
        GPlayer kickee = readNextArg();

        // Prepare

        // Can only kick a player from your guild
        if (kickee.getGuild() != guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player not in sender guild", gPlayer.describe(kickee)));

        // Can only kick the player if you are a higher rank than them
        GuildRank rankToKick = GuildRank.getGuildRankByLevel(Math.min(kickee.getGuildRank().getLevel() + 1, GuildRank.LEADER.getLevel()));
        if (gPlayer.getGuildRank().getLevel() < rankToKick.getLevel()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.kick.rank not higher", util.formatEnum(rankToKick), gPlayer.describe(kickee)));

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.player kicked", guild.describe(gPlayer), guild.describe(kickee)), kickee);

        // Apply

        // Update gPlayer
        kickee.leftGuild();

        // Call event
        GPlayerGuildChangedEvent gPlayerGuildChangedEvent = new GPlayerGuildChangedEvent(kickee, guild, null, GPlayerGuildChangedEvent.Reason.KICKED);
        gPlayerGuildChangedEvent.run();

        // Inform

        // Inform kickee
        kickee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.kick.success to kickee", kickee.describe(guild)));

        // Inform kicker
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.kick.success", gPlayer.describe(kickee)));

    }
}
