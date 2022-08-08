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
            oldLeader.sendFailMsg(Messages.getMsg("commands.not in guild", oldLeader, null, String.join(" ", args)));
            return;
        }

        if (oldLeader.getGuildRank() != GuildRank.LEADER) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.guild rank too low", oldLeader, null, String.join(" ", args)));
            return;
        }

        Guild guild = oldLeader.getGuild();
        gPlayer newLeader = gPlayers.get(args[0]);

        if (newLeader == null) {
            oldLeader.sendFailMsg(Messages.getMsg("commands.promote.promotee not found", oldLeader, null, String.join(" ", args)));
            return;
        }

        // Set newLeader to leader
        guild.setLeader(oldLeader, newLeader);

        // Update gPlayers
        oldLeader.setGuildRank(GuildRank.COLEADER);
        newLeader.setGuildRank(GuildRank.LEADER);

        // Inform newLeader
        newLeader.sendNotifyMsg(Messages.getMsg("commands.leader.new leader success msg", oldLeader, newLeader, String.join(" ", args)));

        // Inform
        oldLeader.sendSuccessMsg(Messages.getMsg("commands.leader.old leader success msg", oldLeader, newLeader, String.join(" ", args)));
    }
}
