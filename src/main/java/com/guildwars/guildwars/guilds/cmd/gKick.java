package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;

public class gKick extends gCommand{

    public gKick() {
        super("kick");
        this.setMinArgs(1);
    }

    @Override
    public void perform(gPlayer kicker, String[] args) {

        // Checks
        if (!kicker.isInGuild()) {
            kicker.sendFailMsg(Messages.getMsg("commands.invite.invitee not found"));
            return;
        }

        Guild guild = kicker.getGuild();

        gPlayer kickee = gPlayersIndex.get().getByName(args[0]);

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

        GuildRank kickerRank = kicker.getGuildRank();
        GuildRank kickeeRank = kickee.getGuildRank();
        if (kickerRank.level <= kickeeRank.level) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.guild rank not higher"));
            return;
        }

        // Kick player
        guild.removePlayer(kickee);

        // Save data
        GuildData.get().save(guild);

        // Update gPlayer
        kickee.leftGuild();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.player kicked", kicker, kickee));

        // Call event
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(kickee, null, PlayerGuildChangeEvent.Reason.KICKED);
        playerGuildChangeEvent.run();

        // Inform kickee
        kickee.sendNotifyMsg(Messages.getMsg("commands.kick.kickee kicked msg", guild));

        // Inform kicker
        kicker.sendSuccessMsg(Messages.getMsg("commands.kick.successfully kicked", kickee));
    }
}
