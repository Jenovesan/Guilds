package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;

public class gLeader extends gCommand{

    public gLeader() {
        super("leader");
        setMinArgs(1);
        mustBeInGuild(true);
    }

    @Override
    public void perform(gPlayer oldLeader, String[] args) {
        // Checks
        if (oldLeader.getGuildRank() != GuildRank.LEADER) {
            oldLeader.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.guild rank too low"));
            return;
        }

        Guild guild = oldLeader.getGuild();
        gPlayer newLeader = gPlayersIndex.get().getByName(args[0]);

        if (newLeader == null) {
            oldLeader.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.promote.promotee not found", args[0]));
            return;
        }

        if (newLeader == oldLeader) {
            oldLeader.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.leader.new leader is sender"));
            return;
        }

        if (newLeader.getGuild() != guild) {
            oldLeader.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not in your guild", newLeader));
            return;
        }

        // Change GuildRanks
        newLeader.setGuildRank(GuildRank.LEADER);
        oldLeader.setGuildRank(GuildRank.COLEADER);

        // Save data
        PlayerData.get().save(newLeader);
        PlayerData.get().save(oldLeader);

        // Call Events
        PlayerGuildRankChangeEvent newLeaderGuildRankChangeEvent = new PlayerGuildRankChangeEvent(newLeader, GuildRank.LEADER);
        newLeaderGuildRankChangeEvent.run();
        PlayerGuildRankChangeEvent oldLeaderGuildRankChangeEvent = new PlayerGuildRankChangeEvent(oldLeader, GuildRank.COLEADER);
        oldLeaderGuildRankChangeEvent.run();

        // Inform guild
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.gave leadership", oldLeader, newLeader));

        // Inform newLeader
        newLeader.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.leader.new leader success msg", guild));

        // Inform
        oldLeader.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.leader.old leader success msg", newLeader, guild));
    }
}
