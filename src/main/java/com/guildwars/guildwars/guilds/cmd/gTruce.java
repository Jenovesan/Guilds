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
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.RELATIONS, true)) {
            return;
        }

        Guild guildToTruce;
        gPlayer possiblePlayerToTruce = gPlayers.get(args[0]);
        if (possiblePlayerToTruce != null) { // Player using player name to truce guild
            if (!possiblePlayerToTruce.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.truce.player not in guild", possiblePlayerToTruce));
                return;
            }
            guildToTruce = possiblePlayerToTruce.getGuild();
        } else { //Player using guild name to truce guild
            guildToTruce = Guilds.get(args[0]);
            if (guildToTruce == null) {
                player.sendFailMsg(Messages.getMsg("commands.truce.not a guild or player", args[0]));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (!playerGuild.isEnemied(guildToTruce)) {
            player.sendFailMsg(Messages.getMsg("commands.truce.not enemied", guildToTruce));
            return;
        }

        if (playerGuild.hasTruceRequestWith(guildToTruce)) {
            player.sendFailMsg(Messages.getMsg("commands.truce.already sent truce request", guildToTruce));
            return;
        }

        // Send a truce request
        if (!guildToTruce.hasTruceRequestWith(playerGuild)) {
            playerGuild.sendTruceRequest(player, guildToTruce);

            // Inform
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully sent truce request", guildToTruce));
        }
        // Truce guild
        else {
            playerGuild.truce(guildToTruce);

            // Inform
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully truced", guildToTruce));
        }
    }
}
