package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;

public class gDeInvite extends gCommand{

    public gDeInvite() {
        super("deinvite");
        setMinArgs(1);
        mustBeInGuild(true);
        setMinPermission(GuildPermission.INVITE);
    }

    @Override
    public void perform(gPlayer deInviter, String[] args) {

        Guild deInviterGuild = deInviter.getGuild();

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
