package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class gInvite extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.invite.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.invite.usage");
    }

    @Override
    public int getMinArgs() { return 1; }

    @Override
    public void perform(gPlayer inviter, String[] args) {

        gPlayer invitee = gPlayers.get(args[0]);

        // Checks
        if (invitee == null) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee not found", inviter, null, String.join(" ", args)));
            return;
        }

        if (!inviter.isInGuild()) {
            inviter.sendFailMsg(Messages.getMsg("commands.not in guild", inviter, invitee, String.join(" ", args)));
            return;
        }

        if (!gUtil.checkPermission(inviter, GuildPermission.INVITE, true)) {
            return;
        }

        Guild inviterGuild = inviter.getGuild();

        if (inviterGuild.isInvited(invitee)) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.already invited", inviter, invitee, String.join(" ", args)));
            return;
        }


        if (invitee.isInGuild()) {
            // Invitee is already a member of the inviter's Guild
            if (invitee.getGuild() == inviterGuild) {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in inviter guild", inviter, invitee, String.join(" ", args)));
                return;
            }
            // Invitee not in Guild
            else {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in guild", inviter, invitee, String.join(" ", args)));
                return;
            }
        }

        // Invite player
        inviterGuild.invite(inviter, invitee);

        // Inform invitee
        invitee.sendNotifyMsg(Messages.getMsg("commands.invite.invitee invite msg", inviter, invitee, String.join(" ", args)));

        // Inform inviter
        inviter.sendSuccessMsg(Messages.getMsg("commands.invite.successfully invited", inviter, invitee, String.join(" ", args)));

    }
}
