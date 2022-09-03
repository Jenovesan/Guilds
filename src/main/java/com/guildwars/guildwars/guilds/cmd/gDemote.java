package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;
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

        gPlayer demotee = gPlayersIndex.get().getByName(args[0]);

        if (demotee == null) {
            demoter.sendFailMsg(Messages.getMsg("commands.player not found", args[0]));
            return;
        }

        Guild guild = demoter.getGuild();

        if (demotee.getGuild() != guild) {
            demoter.sendFailMsg(Messages.getMsg("commands.player not in your guild", demotee));
            return;
        }

        GuildRank demoterGuildRank = demoter.getGuildRank();
        GuildRank demoteeGuildRank = demotee.getGuildRank();
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
        demotee.setGuildRank(newGuildRank);

        // Save data
        PlayerData.get().save(demotee);

        // Call Event
        PlayerGuildRankChangeEvent playerGuildRankChangeEvent = new PlayerGuildRankChangeEvent(demotee, newGuildRank);
        playerGuildRankChangeEvent.run();

        // Inform demotee
        demotee.sendNotifyMsg(Messages.getMsg("commands.demote.demotee demoted msg", newGuildRank));

        // Inform demoter
        demoter.sendSuccessMsg(Messages.getMsg("commands.demote.successfully demoted", demotee, newGuildRank));
    }
}
