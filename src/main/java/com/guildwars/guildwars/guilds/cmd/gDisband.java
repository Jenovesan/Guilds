package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.utils.util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;

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
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (player.getGuildRank() != GuildRank.LEADER) {
            player.sendFailMsg(Messages.getMsg("commands.guild rank too low", player, null, util.formatEnum(GuildRank.LEADER)));
            return;
        }

        // Disband Guild
        Guild guild = player.getGuild();
        guild.disband(player);

        // Call GuildDisbandEvent
        Bukkit.getServer().getPluginManager().callEvent(new GuildDisbandEvent(guild));

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded", player, null, String.join(" ", args)));

    }
}
