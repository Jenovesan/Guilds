package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
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
            demoter.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        Guild guild = demoter.getGuild();

        gPlayer demotee = guild.getPlayer(args[0]);

        if (demotee == null) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.demotee not found", args[0]));
            return;
        }

        GuildRank demoterGuildRank = demoter.getGuildRank();
        GuildRank demoteeGuildRank = guild.getGuildRank(demotee);
        if (GuildRank.higherByAmount(demoterGuildRank, demoteeGuildRank) < 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.rank not high enough", demotee));
            return;
        }

        if (demoteeGuildRank.level == 1) {
            demoter.sendFailMsg(Messages.getMsg("commands.demote.cannot demote any further", demotee));
            return;
        }

        // demote demotee
        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(demoteeGuildRank.level - 1);
        guild.changeGuildRank(demotee, newGuildRank);

        // Update gPlayer & Inform demotee if online
        demotee.setGuildRank(newGuildRank);

        // Call Event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildRankChangeEvent(demotee, newGuildRank));

        // Inform demotee
        demotee.sendNotifyMsg(Messages.getMsg("commands.demote.demotee demoted msg", newGuildRank));

        // Inform demoter
        demoter.sendSuccessMsg(Messages.getMsg("commands.demote.successfully demoted", demotee, newGuildRank));
    }
}
