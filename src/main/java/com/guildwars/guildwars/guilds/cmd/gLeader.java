package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class gLeader extends gCommand{

    @Override
    public String getDescription() {
        return Messages.getMsg("commands.leader.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.leader.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer oldLeader, String[] args) {
        // Checks
        if (!oldLeader.isInGuild()) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (oldLeader.getGuildRank() != GuildRank.LEADER) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.guild rank too low"));
            return;
        }

        Guild guild = oldLeader.getGuild();
        gPlayer newLeader = gPlayersIndex.get().getByName(args[0]);

        if (newLeader == null) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.promote.promotee not found", args[0]));
            return;
        }

        if (newLeader == oldLeader) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.leader.new leader is sender"));
            return;
        }

        if (newLeader.getGuild() != guild) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.player not in your guild", newLeader));
            return;
        }

        // Change GuildRanks
        newLeader.setGuildRank(GuildRank.LEADER);
        oldLeader.setGuildRank(GuildRank.COLEADER);

        // Update gPlayers
        oldLeader.setGuildRank(GuildRank.COLEADER);
        newLeader.setGuildRank(GuildRank.LEADER);

        // Call Events
        PlayerGuildRankChangeEvent newLeaderGuildRankChangeEvent = new PlayerGuildRankChangeEvent(newLeader, GuildRank.LEADER);
        newLeaderGuildRankChangeEvent.run();
        PlayerGuildRankChangeEvent oldLeaderGuildRankChangeEvent = new PlayerGuildRankChangeEvent(oldLeader, GuildRank.COLEADER);
        oldLeaderGuildRankChangeEvent.run();

        // Inform guild
        guild.sendAnnouncement(Messages.getMsg("guild announcements.gave leadership", oldLeader, newLeader));

        // Inform newLeader
        newLeader.sendNotifyMsg(Messages.getMsg("commands.leader.new leader success msg", guild));

        // Inform
        oldLeader.sendSuccessMsg(Messages.getMsg("commands.leader.old leader success msg", newLeader, guild));
    }
}
