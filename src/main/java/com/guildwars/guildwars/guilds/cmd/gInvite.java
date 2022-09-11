package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import org.bukkit.scheduler.BukkitRunnable;

public class gInvite extends gCommand{

    public gInvite() {
        super("invite");
        setMinArgs(1);
        mustBeInGuild(true);
        setMinPermission(GuildPermission.INVITE);
    }

    @Override
    public void perform(gPlayer inviter, String[] args) {

        gPlayer invitee = gPlayersIndex.get().getByName(args[0]);

        if (invitee == null) {
            inviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not found", args[0]));
            return;
        }

        Guild inviterGuild = inviter.getGuild();

        if (inviterGuild.isInvited(invitee)) {
            inviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.invite.already invited", invitee));
            return;
        }


        if (invitee.isInGuild()) {
            // Invitee is already a member of the inviter's Guild
            if (invitee.getGuild() == inviterGuild) {
                inviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.invite.invitee in inviter guild", invitee));
                return;
            }
            // Invitee not in Guild
            else {
                inviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.invite.invitee in guild", invitee));
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
                    invitee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.invite.invite expired", inviterGuild));
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get(Plugin.GUILDS).getInt("invite expire time (s)") * 20L);

        // Inform invitee
        invitee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.invite.invitee invite msg", inviterGuild));

        // Inform inviter
        inviter.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.invite.successfully invited", invitee));

    }
}
