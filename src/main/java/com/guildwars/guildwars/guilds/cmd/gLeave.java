package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.Bukkit;

public class gLeave extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.leave.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.leave.usage");
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

        if (player.getGuildRank() == GuildRank.LEADER) {
            player.sendFailMsg(Messages.getMsg("commands.leave.is leader"));
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
        guild.sendAnnouncement(Messages.getMsg("guild announcements.player leave", player));

        // Call event
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(player, null, PlayerGuildChangeEvent.Reason.LEAVE);
        playerGuildChangeEvent.run();

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.leave.successfully left", guild));
    }
}
