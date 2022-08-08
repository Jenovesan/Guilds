package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
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
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (player.getGuildRank() == GuildRank.LEADER) {
            player.sendFailMsg(Messages.getMsg("commands.leave.is leader", player, null, String.join(" ", args)));
            return;
        }

        //Leave guild
        Guild guild = player.getGuild();
        guild.removePlayer(player);

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(player, null, PlayerGuildChangeEvent.Reason.LEAVE));

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.leave.successfully left", player, null, String.join(" ", args)));
    }
}
