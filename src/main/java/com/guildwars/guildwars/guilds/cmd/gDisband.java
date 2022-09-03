package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.util;

public class gDisband extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.disband.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.disband.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }


    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (player.getGuildRank() != GuildRank.LEADER) {
            player.sendFailMsg(Messages.getMsg("commands.guild rank too low", util.formatEnum(GuildRank.LEADER)));
            return;
        }

        // Disband Guild
        Guild guild = player.getGuild();

        guild.disband();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.disband"));

        // Call GuildDisbandEvent
        GuildDisbandEvent guildDisbandEvent = new GuildDisbandEvent(guild);
        guildDisbandEvent.run();

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded", guild));

    }
}
