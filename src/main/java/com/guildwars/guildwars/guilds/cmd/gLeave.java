package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
    public void perform(gPlayer leaver, String[] args) {

        // Checks
        if (!leaver.isInGuild()) {
            leaver.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (leaver.getGuildRank() == GuildRank.LEADER) {
            leaver.sendFailMsg(Messages.getMsg("commands.leave.is leader"));
            return;
        }

        //Leave guild
        Guild guild = leaver.getGuild();
        Player leaverPlayer = leaver.getPlayer();
        guild.removePlayer(leaverPlayer);

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(leaverPlayer, null, PlayerGuildChangeEvent.Reason.LEAVE));

        // Inform player
        leaver.sendSuccessMsg(Messages.getMsg("commands.leave.successfully left", leaverPlayer, null, args, guild, null, leaver.getGuildRank(), null));
    }
}
