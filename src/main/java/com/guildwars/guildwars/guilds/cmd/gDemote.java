package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.utils.pUtil;
import com.guildwars.guildwars.utils.util;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

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
            demoter.sendFailMsg(Messages.getMsg("commands.demote.not in guild"));
            return;
        }

        Guild guild = demoter.getGuild();

        OfflinePlayer demotee = guild.getOfflinePlayer(args[0]);

        if (demotee == null) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.demotee not found").replace("<input>", args[0]));
            return;
        }

        GuildRank demoterGuildRank = demoter.getGuildRank();
        GuildRank demoteeGuildRank = guild.getGuildRank(demotee);
        if (GuildRank.higherByAmount(demoterGuildRank, demoteeGuildRank) < 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.rank not high enough").replace("<name>", Objects.requireNonNullElse(demotee.getName(), "")));
            return;
        }

        if (demoteeGuildRank.level == 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.cannot demote any further").replace("<name>", Objects.requireNonNullElse(demotee.getName(), "")));
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
            pUtil.sendSuccessMsg(demoteePlayer, Messages.getMsg("commands.demote.demotee demoted msg").replace("<rank>", util.formatEnum(newGuildRank)));
        }

        // Inform demoter
        demoter.sendSuccessMsg(Messages.getMsg("commands.demote.successfully demoted").replace("<name>", Objects.requireNonNullElse(demotee.getName(), "")).replace("<rank>", util.formatEnum(newGuildRank)));
    }
}
