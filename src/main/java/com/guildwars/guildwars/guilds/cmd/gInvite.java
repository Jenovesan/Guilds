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

        Player invitee = Bukkit.getPlayerExact(args[0]);

        // Checks
        if (invitee == null) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee not found", inviter.getPlayer(), null, args, inviter.getGuild(), null, inviter.getGuildRank(), null));
            return;
        }

        if (!inviter.isInGuild()) {
            inviter.sendFailMsg(Messages.getMsg("commands.not in guild", inviter.getPlayer(), invitee, args, null, null, null, null));
            return;
        }

        if (!gUtil.checkPermission(inviter, GuildPermission.INVITE)) {
            return;
        }

        Guild inviterGuild = inviter.getGuild();

        if (inviterGuild.isInvited(invitee)) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.already invited", inviter.getPlayer(), invitee, args, inviterGuild, inviterGuild, inviter.getGuildRank(), null));
            return;
        }

        gPlayer gInvitee = gPlayers.get(invitee);
        if (gInvitee.isInGuild()) {
            // Invitee is already a member of the inviter's Guild
            if (gInvitee.getGuild() == inviterGuild) {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in inviter guild", inviter.getPlayer(), invitee, args, inviterGuild, inviterGuild, inviter.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
                return;
            }
            // Invitee not in Guild
            else {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in guild", inviter.getPlayer(), invitee, args, inviterGuild, gInvitee.getGuild(), inviter.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
                return;
            }
        }

        // Invite player
        inviterGuild.invite(inviter.getPlayer(), invitee);

        // Inform invitee
        pUtil.sendNotifyMsg(invitee, Messages.getMsg("commands.invite.invitee invite msg", invitee, inviter.getPlayer(), args, null, inviterGuild, null, GuildRank.valueOf(Config.get().getString("join guild at rank"))));

        // Inform inviter
        inviter.sendSuccessMsg(Messages.getMsg("commands.invite.successfully invited", inviter.getPlayer(), invitee, args, inviterGuild, inviterGuild, inviter.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));

    }
}
