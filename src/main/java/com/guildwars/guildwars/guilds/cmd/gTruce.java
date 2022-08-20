package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.scheduler.BukkitRunnable;

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
            guildToTruce = GuildsIndex.getGuildByName(args[0]);
            if (guildToTruce == null) {
                player.sendFailMsg(Messages.getMsg("commands.not a guild or player", args[0]));
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
            // Add to truce request list
            playerGuild.sendTruceRequest(guildToTruce);

            // Inform GuildToTruce
            guildToTruce.sendAnnouncement(Messages.getMsg("guild announcements.received truce request", playerGuild));

            // Inform playerGuild
            playerGuild.sendAnnouncement(Messages.getMsg("guild announcements.sent truce request", player, guildToTruce));

            // Remove truce request later
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Remove truce request
                    playerGuild.removeTruceRequest(guildToTruce);
                }
            }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("truce request expire time (m)") * 1200L);

            // Inform
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully sent truce request", guildToTruce));
        }
        // Truce guild
        else {
            // Have playerGuild truce guildToTruce
            playerGuild.truce(guildToTruce);

            // Have guildToTruce truce playerGuild
            guildToTruce.truce(playerGuild);

            // Inform playerGuild
            playerGuild.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", guildToTruce));

            // Inform guildToTruce
            guildToTruce.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", playerGuild));

            // Remove guildToTruce's truce request with this guild
            guildToTruce.removeTruceRequest(playerGuild);

            // Inform player
            player.sendSuccessMsg(Messages.getMsg("commands.truce.successfully truced", guildToTruce));
        }
    }
}
