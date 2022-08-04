package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
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
    public void perform(gPlayer gPlayer, String[] args) {
        // Checks
        if (gPlayer.getGuildRank() != GuildRank.LEADER) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.guild rank too low"));
            return;
        }

        // Disband Guild
        Guild guild = gPlayer.getGuild();

        // Used to update gPlayers & inform guild members later
        HashSet<Player> onlineGuildMembers = guild.getOnlinePlayers();

        guild.disband();

        // Update gPlayers & call event
        for (Player onlineGuildMember : onlineGuildMembers) {
            // Update gPlayer
            gPlayers.get(onlineGuildMember).leftGuild();
            // Call events
            Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(onlineGuildMember, null, PlayerGuildChangeEvent.Reason.DISBAND));
            Bukkit.getServer().getPluginManager().callEvent(new GuildDisbandEvent(guild));
        }

        // Inform
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded"));

    }
}
