package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
            deInviter.sendFailMsg(Messages.getMsg("commands.not in guild", deInviter, null, String.join(" ", args)));
            return;
        }
        Guild deInviterGuild = deInviter.getGuild();

        if (!gUtil.checkPermission(deInviter, GuildPermission.INVITE)) {
            return;
        }

        String deInviteeName = args[0];
        gPlayer deInvitee = deInviterGuild.getInvitedPlayer(deInviteeName);

        if (deInvitee == null) {
            deInviter.sendFailMsg(Messages.getMsg("commands.deinvite.not invited", null, null, String.join(" ", args)));
            return;
        }

        // Deinvite player
        deInviterGuild.deInvite(deInvitee);

        // Inform deinvitee
        deInvitee.sendNotifyMsg(Messages.getMsg("commands.deinvite.deinvitee deinvited msg", deInviter, deInvitee, String.join(" ", args)));

        // Inform deinviter
        deInviter.sendSuccessMsg(Messages.getMsg("commands.deinvite.successfully deinvited", deInviter, deInvitee, String.join(" ", args)));
    }
}
