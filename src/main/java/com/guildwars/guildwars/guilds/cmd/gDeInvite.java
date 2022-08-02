package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildPermission;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

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
            deInviter.sendFailMsg(Messages.getMsg("commands.deinvite.deinviter not in guild"));
            return;
        }
        Guild deInviterGuild = deInviter.getGuild();

        if (!gUtil.checkPermission(deInviter, GuildPermission.INVITE)) {
            return;
        }

        String deInviteeName = args[0];
        OfflinePlayer deInvitee = deInviterGuild.getInvitedPlayer(deInviteeName);

        if (deInvitee == null) {
            deInviter.sendFailMsg(Messages.getMsg("commands.deinvite.not invited").replace("<input>", args[0]));
            return;
        }

        // Deinvite player
        deInviterGuild.deInvite(deInvitee);

        // Inform deinvitee
        Player deInviteePlayer = deInvitee.getPlayer();
        if (deInviteePlayer != null) {
            pUtil.sendNotifyMsg(deInviteePlayer, Messages.getMsg("commands.deinvite.deinvitee deinvited msg").replace("<name>", deInviterGuild.getName()));
        }

        // Inform deinviter
        deInviter.sendSuccessMsg(Messages.getMsg("commands.deinvite.successfully deinvited").replace("<name>", Objects.requireNonNullElse(deInvitee.getName(), args[0])));
    }
}
