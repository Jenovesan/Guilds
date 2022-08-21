package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

        // Checks
        if (!inviter.isInGuild()) {
            inviter.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        gPlayer invitee = gPlayersIndex.get().getByName(args[0]);

        if (invitee == null) {
            inviter.sendFailMsg(Messages.getMsg("commands.player not found", args[0]));
            return;
        }

        if (!gUtil.checkPermission(inviter, GuildPermission.INVITE, true)) {
            return;
        }

        Guild inviterGuild = inviter.getGuild();

        if (inviterGuild.isInvited(invitee)) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.already invited", invitee));
            return;
        }


        if (invitee.isInGuild()) {
            // Invitee is already a member of the inviter's Guild
            if (invitee.getGuild() == inviterGuild) {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in inviter guild", invitee));
                return;
            }
            // Invitee not in Guild
            else {
                inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in guild", invitee));
                return;
            }
        }

        // Invite player
        inviterGuild.invite(invitee);

        // Remove player invitation later
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if invitee still has an invitation
                if (!inviterGuild.isInvited(invitee)) {
                    return;
                }

                // Remove player invitation
                inviterGuild.removeInvite(invitee);

                // Notify player if they're online
                if (invitee.getPlayer() != null) {
                    invitee.sendNotifyMsg(Messages.getMsg("commands.invite.invite expired", inviterGuild));
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("invite expire time (s)") * 20L);

        // Inform invitee
        invitee.sendNotifyMsg(Messages.getMsg("commands.invite.invitee invite msg", inviterGuild));

        // Inform inviter
        inviter.sendSuccessMsg(Messages.getMsg("commands.invite.successfully invited", invitee));

    }
}
