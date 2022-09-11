package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;

public class gDeInvite extends gCommand{

    public gDeInvite() {
        super("deinvite");
        this.setMinArgs(1);
    }

    @Override
    public void perform(gPlayer deInviter, String[] args) {

        // Checks
        if (!deInviter.isInGuild()) {
            deInviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
            return;
        }
        Guild deInviterGuild = deInviter.getGuild();

        if (!gUtil.checkPermission(deInviter, GuildPermission.INVITE, true)) {
            return;
        }

        gPlayer deInvitee = gPlayersIndex.get().getByName(args[0]);

        if (deInvitee == null) {
            deInviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not found", args[0]));
            return;
        }

        if (deInviterGuild.isInvited(deInvitee)) {
            deInviter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not invited", deInvitee));
            return;
        }

        // Deinvite player
        deInviterGuild.removeInvite(deInvitee);

        // Inform deinvitee
        deInvitee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.deinvite.deinvitee deinvited msg", deInviterGuild));

        // Inform deinviter
        deInviter.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.deinvite.successfully deinvited", deInvitee));
    }
}
