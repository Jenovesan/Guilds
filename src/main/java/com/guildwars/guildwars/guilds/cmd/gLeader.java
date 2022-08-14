package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
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
        gPlayer newLeader = gPlayers.get(args[0]);

        if (newLeader == null) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.promote.promotee not found", args[0]));
            return;
        }

        if (newLeader == oldLeader) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.leader.new leader is sender"));
            return;
        }

        if (newLeader.getGuild() != guild) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.leader.new leader is not in guild", newLeader));
            return;
        }

        // Set newLeader to leader
        guild.setLeader(oldLeader, newLeader);

        // Update gPlayers
        oldLeader.setGuildRank(GuildRank.COLEADER);
        newLeader.setGuildRank(GuildRank.LEADER);

        // Call Event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildRankChangeEvent(newLeader, GuildRank.LEADER));

        // Inform guild
        guild.sendAnnouncement(Messages.getMsg("guild announcements.gave leadership", oldLeader, newLeader));

        // Inform newLeader
        newLeader.sendNotifyMsg(Messages.getMsg("commands.leader.new leader success msg", guild));

        // Inform
        oldLeader.sendSuccessMsg(Messages.getMsg("commands.leader.old leader success msg", newLeader, guild));
    }
}
