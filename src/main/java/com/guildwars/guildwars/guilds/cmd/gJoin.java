package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class gJoin extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.join.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.join.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer joiner, String[] args) {
        // Checks
        if (joiner.isInGuild()) {
            joiner.sendFailMsg(Messages.getMsg("commands.join.in guild", joiner.getPlayer(), null, args, joiner.getGuild(), null, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
            return;
        }

        Guild guildToJoin;
        Player possiblePlayerToJoin = Bukkit.getPlayerExact(args[0]);
        if (possiblePlayerToJoin != null) { // Player using player name to join guild
            gPlayer gPlayerToJoin = gPlayers.get(possiblePlayerToJoin);
            if (!gPlayerToJoin.isInGuild()) {
                joiner.sendFailMsg(Messages.getMsg("commands.join.player not in guild", joiner.getPlayer(), possiblePlayerToJoin, args, null, null, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
                return;
            }
            guildToJoin = gPlayerToJoin.getGuild();
        } else { //Player using guild name to join guild
            guildToJoin = Guilds.get(args[0]);
            if (guildToJoin == null) {
                joiner.sendFailMsg(Messages.getMsg("commands.join.not a guild or player", joiner.getPlayer(), possiblePlayerToJoin, args, null, guildToJoin, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
                return;
            }
        }

        if (!guildToJoin.isInvited(joiner.getPlayer())) {
            joiner.sendFailMsg(Messages.getMsg("commands.join.not invited", joiner.getPlayer(), possiblePlayerToJoin, args, null, guildToJoin, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
            return;
        }

        if (guildToJoin.isFull()) {
            joiner.sendFailMsg(Messages.getMsg("commands.join.guild is full", joiner.getPlayer(), possiblePlayerToJoin, args, null, guildToJoin, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
            // Send guild announcement saying player tried to join, but guild was full
            guildToJoin.sendAnnouncement(Messages.getMsg("guilds announcements.guild was full", joiner.getPlayer(), possiblePlayerToJoin, args, null, guildToJoin, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
            return;
        }

        // Join guild
        Player player = joiner.getPlayer();
        // Naming preference
        Guild newGuild = guildToJoin;

        newGuild.addPlayer(player);

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(joiner.getPlayer(), newGuild, PlayerGuildChangeEvent.Reason.JOIN));

        // Remove invite
        newGuild.deInvite(player);

        // Inform player
        joiner.sendSuccessMsg(Messages.getMsg("commands.join.successfully joined", joiner.getPlayer(), possiblePlayerToJoin, args, null, guildToJoin, joiner.getGuildRank(), GuildRank.valueOf(Config.get().getString("join guild at rank"))));
    }
}
