package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Invitation;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gDeInvite extends gCommand{

    public gDeInvite() {
        // Name
        super("deinvite");

        // Aliases
        addAlias("deinv");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.INVITE));

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        GPlayer deInvitee = readNextArg();

        // Prepare

        Invitation invite = guild.getInvite(deInvitee);

        // Cannot de-invite deInvitee if deInvitee is not invited to the guild
        if (invite == null) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.deinvite.not invited", gPlayer.describe(deInvitee)));

        // Apply

        guild.removeInvite(invite);

        // Inform

        // Inform deinvitee
        deInvitee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.deinvite.success to deinvitee", deInvitee.describe(guild)));

        // Inform deinviter
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.deinvite.success", gPlayer.describe(deInvitee)));

    }
}
