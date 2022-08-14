package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class gKick extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.kick.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.kick.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer kicker, String[] args) {

        // Checks
        if (!kicker.isInGuild()) {
            kicker.sendFailMsg(Messages.getMsg("commands.invite.invitee not found"));
            return;
        }

        Guild guild = kicker.getGuild();

        gPlayer kickee = gPlayers.get(args[0]);

        if (kickee == null) {
            kicker.sendFailMsg(Messages.getMsg("commands.player not found", args[0]));
            return;
        }

        if (!gUtil.checkPermission(kicker, GuildPermission.INVITE, true)) {
            return;
        }

        if (kickee.getGuild() != guild) {
            kicker.sendFailMsg(Messages.getMsg("commands.player not in your guild", kickee));
            return;
        }

        GuildRank kickerRank = guild.getRank(kicker);
        GuildRank kickeeRank = guild.getRank(kickee);
        if (kickerRank.level <= kickeeRank.level) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.guild rank not higher"));
            return;
        }

        // Kick player
        guild.kickPlayer(kickee);

        // Update gPlayer
        kickee.leftGuild();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.player kicked", kicker, kickee));

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(kickee, null, PlayerGuildChangeEvent.Reason.KICKED));

        // Inform kickee
        kickee.sendNotifyMsg(Messages.getMsg("commands.kick.kickee kicked msg", guild));

        // Inform kicker
        kicker.sendSuccessMsg(Messages.getMsg("commands.kick.successfully kicked", kickee));
    }
}
