package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class gTruce extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.truce.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.truce.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player.getPlayer(), null, args, null, null, null, null));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.RELATIONS)) {
            return;
        }

        Guild guildToTruce;
        Player possiblePlayerToTruce = Bukkit.getPlayerExact(args[0]);
        if (possiblePlayerToTruce != null) { // Player using player name to truce guild
            gPlayer gPlayerToTruce = gPlayers.get(possiblePlayerToTruce);
            if (!gPlayerToTruce.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.truce.player not in guild", player.getPlayer(), possiblePlayerToTruce, args, player.getGuild(), null, player.getGuildRank(), gPlayerToTruce.getGuildRank()));
                return;
            }
            guildToTruce = gPlayerToTruce.getGuild();
        } else { //Player using guild name to truce guild
            guildToTruce = Guilds.get(args[0]);
            if (guildToTruce == null) {
                player.sendFailMsg(Messages.getMsg("commands.truce.not a guild or player", player.getPlayer(), null, args, player.getGuild(), null, player.getGuildRank(), null));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (!playerGuild.isEnemied(guildToTruce)) {
            player.sendFailMsg(Messages.getMsg("commands.truce.not enemied", player.getPlayer(), possiblePlayerToTruce, args, playerGuild, guildToTruce, player.getGuildRank(), null));
            return;
        }

        if (playerGuild.hasTruceRequestWith(guildToTruce)) {
            player.sendFailMsg(Messages.getMsg("commands.truce.already sent truce request", player.getPlayer(), possiblePlayerToTruce, args, playerGuild, guildToTruce, player.getGuildRank(), null));
            return;
        }

        // Send a truce request
        if (!guildToTruce.hasTruceRequestWith(playerGuild)) {
            playerGuild.sendTruceRequest(player.getPlayer(), guildToTruce);

            // Inform
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully sent truce request", player.getPlayer(), possiblePlayerToTruce, args, playerGuild, guildToTruce, player.getGuildRank(), null));
        }
        // Truce guild
        else {
            playerGuild.truce(guildToTruce);

            // Inform
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully truced", player.getPlayer(), possiblePlayerToTruce, args, playerGuild, guildToTruce, player.getGuildRank(), null));
        }

    }
}
