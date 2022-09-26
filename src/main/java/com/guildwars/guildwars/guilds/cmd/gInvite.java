package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Invitation;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GPlayerArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gInvite extends gCommand{

    public gInvite() {
        // Name
        super("invite");

        // Aliases
        addAlias("inv");
        addAlias("add");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.INVITE));

        // Args
        addArg(new GPlayerArg(true, false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        GPlayer invitee = readNextArg();

        // Prepare

        // Cannot invite a player if they are already invited to guild
        if (guild.isInvited(invitee)) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.invite.already invited", gPlayer.describe(invitee)));

        // Cannot invite player who is in a guild
        if (invitee.isInGuild()) {
            // If invitee is already a member of the gPlayer's Guild...
            if (invitee.getGuild() == guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.invite.invitee in inviter guild", gPlayer.describe(invitee)));
            else throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.invite.invitee in guild", gPlayer.describe(invitee)));
        }

        // Apply

        Invitation invite = new Invitation(invitee);
        guild.addInvite(invite);

        // Inform

        // Inform invitee
        invitee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.invite.success to invitee", invitee.describe(guild)));

        // Inform inviter
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.invite.success", gPlayer.describe(invitee)));
    }
}
