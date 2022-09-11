package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;

public class gDemote extends gCommand{

    public gDemote() {
        super("demote");
        setMinArgs(1);
        mustBeInGuild(true);
    }

    @Override
    public void perform(gPlayer demoter, String[] args) {

        gPlayer demotee = gPlayersIndex.get().getByName(args[0]);

        if (demotee == null) {
            demoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not found", args[0]));
            return;
        }

        Guild guild = demoter.getGuild();

        if (demotee.getGuild() != guild) {
            demoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not in your guild", demotee));
            return;
        }

        GuildRank demoterGuildRank = demoter.getGuildRank();
        GuildRank demoteeGuildRank = demotee.getGuildRank();
        if (GuildRank.higherByAmount(demoterGuildRank, demoteeGuildRank) < 1) {
            demoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.demote.rank not high enough", demotee));
            return;
        }

        if (demoteeGuildRank.level == 1) {
            demoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.demote.cannot demote any further", demotee));
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
        demotee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.demote.demotee demoted msg", newGuildRank));

        // Inform demoter
        demoter.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.demote.successfully demoted", demotee, newGuildRank));
    }
}
