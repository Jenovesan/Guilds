package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.gPlayer;

public class gLeave extends gCommand{

    public gLeave() {
        super("leave");
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
            return;
        }

        if (player.getGuildRank() == GuildRank.LEADER) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.leave.is leader"));
            return;
        }

        //Leave guild
        Guild guild = player.getGuild();
        guild.removePlayer(player);

        // Save data
        GuildData.get().save(guild);

        // Update gPlayer
        player.leftGuild();

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.player leave", player));

        // Call event
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(player, null, PlayerGuildChangeEvent.Reason.LEAVE);
        playerGuildChangeEvent.run();

        // Inform player
        player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.leave.successfully left", guild));
    }
}
