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
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.join.in guild", player, null, String.join(" ", args)));
            return;
        }

        Guild guildToJoin;
        gPlayer possiblePlayerToJoin = gPlayers.get(args[0]);
        if (possiblePlayerToJoin != null) { // Player using player name to join guild
            if (!possiblePlayerToJoin.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.join.player not in guild", player, possiblePlayerToJoin, String.join(" ", args)));
                return;
            }
            guildToJoin = possiblePlayerToJoin.getGuild();
        } else { //Player using guild name to join guild
            guildToJoin = Guilds.get(args[0]);
            if (guildToJoin == null) {
                player.sendFailMsg(Messages.getMsg("commands.join.not a guild or player", player, null, String.join(" ", args)));
                return;
            }
        }

        if (!guildToJoin.isInvited(player)) {
            player.sendFailMsg(Messages.getMsg("commands.join.not invited", player, possiblePlayerToJoin, String.join(" ", args)));
            return;
        }

        if (guildToJoin.isFull()) {
            player.sendFailMsg(Messages.getMsg("commands.join.guild is full", player, possiblePlayerToJoin, String.join(" ", args)));
            // Send guild announcement saying player tried to join, but guild was full
            guildToJoin.sendAnnouncement(Messages.getMsg("guilds announcements.guild was full", player, possiblePlayerToJoin, String.join(" ", args)));
            return;
        }

        // Join guild
        guildToJoin.addPlayer(player);

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(player, guildToJoin, PlayerGuildChangeEvent.Reason.JOIN));

        // Remove invite
        guildToJoin.deInvite(player);

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.join.successfully joined", player, possiblePlayerToJoin, String.join(" ", args)));
    }
}
