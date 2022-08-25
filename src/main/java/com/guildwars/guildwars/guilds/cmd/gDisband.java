package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.util;
import org.bukkit.Bukkit;

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

        // Remove Guild from Guilds
        Guilds.get().remove(guild);

        // Remove Guild Data
        Guilds.get().removeData(guild);

        // Update gPlayers & call event
        for (gPlayer onlineGuildMember : guild.getOnlinePlayers()) {
            // Update gPlayer
            onlineGuildMember.leftGuild();
            // Call PlayerGuildChangeEvents
            PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(onlineGuildMember, null, PlayerGuildChangeEvent.Reason.DISBAND);
            playerGuildChangeEvent.run();
        }

        // Update board
        for (int[] claimBoardLocation : guild.getClaimLocations()) {
            Board.getBoard()[claimBoardLocation[0]][claimBoardLocation[1]].setWilderness();
        }

        // Remove from index
        GuildsIndex.get().remove(guild);

        // Send Guild Announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.disband"));

        // Call GuildDisbandEvent
        GuildDisbandEvent guildDisbandEvent = new GuildDisbandEvent(guild);
        guildDisbandEvent.run();

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded", guild));

    }
}
