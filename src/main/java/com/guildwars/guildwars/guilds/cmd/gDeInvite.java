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
            deInviter.sendFailMsg(Messages.getMsg("commands.not in guild", deInviter.getPlayer(), null, args, null, null, null, null));
            return;
        }
        Guild deInviterGuild = deInviter.getGuild();

        if (!gUtil.checkPermission(deInviter, GuildPermission.INVITE)) {
            return;
        }

        String deInviteeName = args[0];
        OfflinePlayer deInvitee = deInviterGuild.getInvitedPlayer(deInviteeName);

        if (deInvitee == null) {
            deInviter.sendFailMsg(Messages.getMsg("commands.deinvite.not invited", deInviter.getPlayer(), null, args, deInviterGuild, null, deInviter.getGuildRank(), null));
            return;
        }

        // Deinvite player
        deInviterGuild.deInvite(deInvitee);

        // Inform deinvitee
        Player deInviteePlayer = deInvitee.getPlayer();
        if (deInviteePlayer != null) {
            pUtil.sendNotifyMsg(deInviteePlayer, Messages.getMsg("commands.deinvite.deinvitee deinvited msg", deInviteePlayer, deInviter.getPlayer(), args, deInviterGuild, deInviterGuild, null, GuildRank.valueOf(Config.get().getString("join guild at rank"))));
        }

        // Inform deinviter
        deInviter.sendSuccessMsg(Messages.getMsg("commands.deinvite.successfully deinvited", deInviter.getPlayer(), deInviteePlayer, args, deInviterGuild, deInviterGuild, deInviter.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
    }
}
