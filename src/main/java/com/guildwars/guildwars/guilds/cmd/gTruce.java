package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.scheduler.BukkitRunnable;

public class gTruce extends gCommand{

    public gTruce() {
        super("truce");
        setMinArgs(1);
        mustBeInGuild(true);
        setMinPermission(GuildPermission.RELATIONS);
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        Guild guildToTruce;
        gPlayer possiblePlayerToTruce = gPlayersIndex.get().getByName(args[0]);
        if (possiblePlayerToTruce != null) { // Player using player name to truce guild
            if (!possiblePlayerToTruce.isInGuild()) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.truce.player not in guild", possiblePlayerToTruce));
                return;
            }
            guildToTruce = possiblePlayerToTruce.getGuild();
        } else { //Player using guild name to truce guild
            guildToTruce = GuildsIndex.get().getByName(args[0]);
            if (guildToTruce == null) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not a guild or player", args[0]));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (!playerGuild.isEnemied(guildToTruce)) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.truce.not enemied", guildToTruce));
            return;
        }

        if (playerGuild.hasTruceRequestWith(guildToTruce)) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.truce.already sent truce request", guildToTruce));
            return;
        }

        // Send a truce request
        if (!guildToTruce.hasTruceRequestWith(playerGuild)) {
            // Add to truce request list
            playerGuild.sendTruceRequest(guildToTruce);

            // Inform GuildToTruce
            guildToTruce.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.received truce request", playerGuild));

            // Inform playerGuild
            playerGuild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.sent truce request", player, guildToTruce));

            // Remove truce request later
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Remove truce request
                    playerGuild.removeTruceRequest(guildToTruce);
                }
            }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get(Plugin.GUILDS).getInt("truce request expire time (m)") * 1200L);

            // Inform
            player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.truce.successfully sent truce request", guildToTruce));
        }
        // Truce guild
        else {
            // Have playerGuild truce guildToTruce
            playerGuild.truce(guildToTruce);

            // Have guildToTruce truce playerGuild
            guildToTruce.truce(playerGuild);

            // Save data
            GuildData.get().save(playerGuild);
            GuildData.get().save(guildToTruce);

            // Inform playerGuild
            playerGuild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.truced guild", guildToTruce));

            // Inform guildToTruce
            guildToTruce.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.truced guild", playerGuild));

            // Remove guildToTruce's truce request with this guild
            guildToTruce.removeTruceRequest(playerGuild);

            // Inform player
            player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.truce.successfully truced", guildToTruce));
        }
    }
}
