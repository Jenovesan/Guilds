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
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player.getPlayer(), null, args, null, null, null, null));
            return;
        }

        if (player.getGuildRank() != GuildRank.LEADER) {
            player.sendFailMsg(Messages.getMsg("commands.guild rank too low", player.getPlayer(), null, args, player.getGuild(), player.getGuild(), player.getGuildRank(), GuildRank.LEADER));
            return;
        }

        // Disband Guild
        Guild guild = player.getGuild();

        // Used to update gPlayers & inform guild members later
        HashSet<Player> onlineGuildMembers = guild.getOnlinePlayers();

        guild.disband(player.getPlayer());

        // Update gPlayers & call event
        for (Player onlineGuildMember : onlineGuildMembers) {
            // Update gPlayer
            gPlayers.get(onlineGuildMember).leftGuild();
            // Call PlayerGuildChangeEvents
            Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(onlineGuildMember, null, PlayerGuildChangeEvent.Reason.DISBAND));
        }

        // Call GuildDisbandEvent
        Bukkit.getServer().getPluginManager().callEvent(new GuildDisbandEvent(guild));

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.disband.successfully disbanded", player.getPlayer(), null, args, player.getGuild(), null, player.getGuildRank(), null));

    }
}
