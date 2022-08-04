package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class gDemote extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.demote.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.demote.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer demoter, String[] args) {
        // Checks
        if (!demoter.isInGuild()) {
            demoter.sendFailMsg(Messages.getMsg("commands.not in guild", demoter.getPlayer(), null, args, null, null, null, null));
            return;
        }

        Guild guild = demoter.getGuild();

        OfflinePlayer demotee = guild.getOfflinePlayer(args[0]);

        if (demotee == null) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.demotee not found", demoter.getPlayer(), null, args, guild, null, null, null));
            return;
        }

        GuildRank demoterGuildRank = demoter.getGuildRank();
        GuildRank demoteeGuildRank = guild.getGuildRank(demotee);
        if (GuildRank.higherByAmount(demoterGuildRank, demoteeGuildRank) < 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.rank not high enough", demoter.getPlayer(), demotee.getPlayer(), args, guild, guild, demoterGuildRank, demoteeGuildRank));
            return;
        }

        if (demoteeGuildRank.level == 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.cannot demote any further", demoter.getPlayer(), demotee.getPlayer(), args, guild, guild, demoterGuildRank, demoteeGuildRank));
            return;
        }

        // demote demotee
        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(demoteeGuildRank.level - 1);
        guild.changeGuildRank(demotee, newGuildRank);

        // Update gPlayer & Inform demotee if online
        Player demoteePlayer = demotee.getPlayer();
        if (demoteePlayer != null) {
            // Update gPlayer
            gPlayer demoteeGPlayer = gPlayers.get(demoteePlayer);
            demoteeGPlayer.setGuildRank(newGuildRank);

            // Inform
            pUtil.sendSuccessMsg(demoteePlayer, Messages.getMsg("commands.demote.demotee demoted msg", demoteePlayer, demoter.getPlayer(), args, guild, guild, demoteeGuildRank, newGuildRank));
        }

        // Inform demoter
        demoter.sendSuccessMsg(Messages.getMsg("commands.demote.successfully demoted", demoter.getPlayer(), demoteePlayer, args, guild, guild, demoterGuildRank, newGuildRank));
    }
}
