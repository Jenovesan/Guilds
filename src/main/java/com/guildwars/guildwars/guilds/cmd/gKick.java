package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;

public class gKick extends gCommand{

    public gKick() {
        super("kick");
        this.setMinArgs(1);
    }

    @Override
    public void perform(gPlayer kicker, String[] args) {

        // Checks
        if (!kicker.isInGuild()) {
            kicker.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.invite.invitee not found"));
            return;
        }

        Guild guild = kicker.getGuild();

        gPlayer kickee = gPlayersIndex.get().getByName(args[0]);

        if (kickee == null) {
            kicker.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not found", args[0]));
            return;
        }

        if (!gUtil.checkPermission(kicker, GuildPermission.INVITE, true)) {
            return;
        }

        if (kickee.getGuild() != guild) {
            kicker.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not in your guild", kickee));
            return;
        }

        GuildRank kickerRank = kicker.getGuildRank();
        GuildRank kickeeRank = kickee.getGuildRank();
        if (kickerRank.level <= kickeeRank.level) {
            kicker.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.kick.guild rank not higher"));
            return;
        }

        // Kick player
        guild.removePlayer(kickee);

        // Save data
        GuildData.get().save(guild);

        // Update gPlayer
        kickee.leftGuild();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.player kicked", kicker, kickee));

        // Call event
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(kickee, null, PlayerGuildChangeEvent.Reason.KICKED);
        playerGuildChangeEvent.run();

        // Inform kickee
        kickee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.kick.kickee kicked msg", guild));

        // Inform kicker
        kicker.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.kick.successfully kicked", kickee));
    }
}
