package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class gPromote extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.promote.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.promote.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer promoter, String[] args) {
        // Checks
        if (!promoter.isInGuild()) {
            promoter.sendFailMsg(Messages.getMsg("commands.not in guild", promoter, null, String.join(" ", args)));
            return;
        }

        Guild guild = promoter.getGuild();

        gPlayer promotee = guild.getPlayer(args[0]);

        if (promotee == null) {
            promoter.sendFailMsg(Messages.getMsg("commands.promote.promotee not found", promoter, null, String.join(" ", args)));
            return;
        }

        GuildRank promoterGuildRank = promoter.getGuildRank();
        GuildRank promoteeGuildRank = guild.getGuildRank(promotee);
        if (GuildRank.higherByAmount(promoterGuildRank, promoteeGuildRank) < 2) {
            if (promoterGuildRank != GuildRank.LEADER) {
                promoter.sendFailMsg(Messages.getMsg("commands.promote.rank not high enough", promoter, promotee, String.join(" ", args)));
            } else { // Tried to promote member to GuildRank: Leader
                promoter.sendFailMsg(Messages.getMsg("commands.promote.tried to make leader", promoter, promotee, String.join(" ", args)));
            }
            return;
        }

        // Promote promotee
        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(promoteeGuildRank.level + 1);
        guild.changeGuildRank(promotee, newGuildRank);

        // Update gPlayer & Inform promotee
        promotee.setGuildRank(newGuildRank);
        // Inform
        promotee.sendNotifyMsg(Messages.getMsg("commands.promote.promotee promoted msg", promoter, promotee, String.join(" ", args)));

        // Inform promoter
        promoter.sendSuccessMsg(Messages.getMsg("commands.promote.successfully promoted", promoter, promotee, String.join(" ", args)));
    }
}
