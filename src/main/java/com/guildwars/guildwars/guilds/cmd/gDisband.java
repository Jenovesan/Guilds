package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
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
    public void perform(gPlayer gPlayer, String[] args) {
        // Checks
        if (gPlayer.getGuildRank() != GuildRank.LEADER) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.guild rank too low"));
            return;
        }

        // Disband Guild
        gPlayer.getGuild().disband();

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(gPlayer.getPlayer(), null, PlayerGuildChangeEvent.Reason.DISBAND));

        // Inform
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded"));

    }
}
