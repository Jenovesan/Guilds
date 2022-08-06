package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.utils.pUtil;
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
            oldLeader.sendFailMsg(Messages.getMsg("commands.not in guild", oldLeader.getPlayer(), null, args, null, null, null, GuildRank.LEADER));
            return;
        }

        if (oldLeader.getGuildRank() != GuildRank.LEADER) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.guild rank too low", oldLeader.getPlayer(), null, args, oldLeader.getGuild(), oldLeader.getGuild(), oldLeader.getGuildRank(), GuildRank.LEADER));
            return;
        }

        Guild guild = oldLeader.getGuild();
        OfflinePlayer newLeader = guild.getOfflinePlayer(args[0]);

        if (newLeader == null) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.promote.promotee not found", oldLeader.getPlayer(), null, args, guild, guild, oldLeader.getGuildRank(), GuildRank.LEADER));
            return;
        }

        // Set newLeader to leader
        guild.setLeader(oldLeader.getPlayer(), newLeader);

        // Update gPlayers
        oldLeader.setGuildRank(GuildRank.COLEADER);

        Player newLeaderPlayer = newLeader.getPlayer();
        if (newLeaderPlayer != null) {
            gPlayers.get(newLeaderPlayer).setGuildRank(GuildRank.LEADER);
            // Inform newLeader
            pUtil.sendSuccessMsg(newLeaderPlayer, Messages.getMsg("commands.leader.new leader success msg", newLeaderPlayer, oldLeader.getPlayer(), args, guild, guild, GuildRank.LEADER, GuildRank.COLEADER));
        }

        // Inform
        oldLeader.sendSuccessMsg(Messages.getMsg("commands.leader.old leader success msg", oldLeader.getPlayer(), newLeaderPlayer, args, guild, guild, GuildRank.COLEADER, GuildRank.LEADER));
    }
}
