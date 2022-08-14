package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;

public class gDeInvite extends gCommand{
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer deInviter, String[] args) {

        // Checks
        if (!deInviter.isInGuild()) {
            deInviter.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }
        Guild deInviterGuild = deInviter.getGuild();

        if (!gUtil.checkPermission(deInviter, GuildPermission.INVITE, true)) {
            return;
        }

        String deInviteeName = args[0];
        gPlayer deInvitee = deInviterGuild.getInvitedPlayer(deInviteeName);

        if (deInvitee == null) {
            deInviter.sendFailMsg(Messages.getMsg("commands.deinvite.not invited", args[0]));
            return;
        }

        // Deinvite player
        deInviterGuild.removeInvite(deInvitee);

        // Inform deinvitee
        deInvitee.sendNotifyMsg(Messages.getMsg("commands.deinvite.deinvitee deinvited msg", deInviterGuild));

        // Inform deinviter
        deInviter.sendSuccessMsg(Messages.getMsg("commands.deinvite.successfully deinvited", deInvitee));
    }
}
