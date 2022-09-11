package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.utils.util;

public class gDisband extends gCommand{

    public gDisband() {
        super("disband");
        mustBeInGuild(true);
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (player.getGuildRank() != GuildRank.LEADER) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.guild rank too low", util.formatEnum(GuildRank.LEADER)));
            return;
        }

        // Disband Guild
        Guild guild = player.getGuild();

        guild.disband();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.disband"));

        // Call GuildDisbandEvent
        GuildDisbandEvent guildDisbandEvent = new GuildDisbandEvent(guild);
        guildDisbandEvent.run();

        // Inform
        player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.disband.successfully disbanded", guild));

    }
}
