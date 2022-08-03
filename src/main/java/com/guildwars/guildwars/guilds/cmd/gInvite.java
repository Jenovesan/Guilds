package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.guilds.gUtil;
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
            inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee not found").replace("<input>", args[0]));
            return;
        }

        if (!inviter.isInGuild()) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.inviter not in guild"));
            return;
        }

        if (!gUtil.checkPermission(inviter, GuildPermission.INVITE)) {
            return;
        }

        if (inviter.getGuild().isInvited(invitee)) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.already invited").replace("<name>", invitee.getName()));
            return;
        }

        gPlayer gInvitee = gPlayers.get(invitee);
        if (gInvitee.isInGuild()) {
            inviter.sendFailMsg(Messages.getMsg("commands.invite.invitee in guild").replace("<name>", invitee.getName()));
            return;
        }

        // Invite player
        Guild inviterGuild = inviter.getGuild();
        inviterGuild.invite(invitee);

        // Inform invitee
        pUtil.sendNotifyMsg(invitee, Messages.getMsg("commands.invite.invitee invite msg").replace("<guild>", inviterGuild.getName()));

        // Inform inviter
        inviter.sendSuccessMsg(Messages.getMsg("commands.invite.successfully invited").replace("<name>", invitee.getName()));

    }
}
