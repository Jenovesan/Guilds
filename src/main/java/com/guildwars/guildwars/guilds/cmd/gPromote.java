package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
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
            promoter.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        gPlayer promotee = gPlayersIndex.get().getByName(args[0]);

        if (promotee == null) {
            promoter.sendFailMsg(Messages.getMsg("commands.player not found", args[0]));
            return;
        }

        Guild guild = promoter.getGuild();

        if (promotee.getGuild() != guild) {
            promoter.sendFailMsg(Messages.getMsg("commands.player not in your guild", promotee));
            return;
        }

        GuildRank promoterGuildRank = promoter.getGuildRank();
        GuildRank promoteeGuildRank = promotee.getGuildRank();
        if (GuildRank.higherByAmount(promoterGuildRank, promoteeGuildRank) < 2) {
            if (promoterGuildRank != GuildRank.LEADER) {
                promoter.sendFailMsg(Messages.getMsg("commands.promote.rank not high enough", promotee));
            } else { // Tried to promote member to GuildRank: Leader
                promoter.sendFailMsg(Messages.getMsg("commands.promote.tried to make leader"));
            }
            return;
        }

        // Promote promotee
        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(promoteeGuildRank.level + 1);

        // Update gPlayer
        promotee.setGuildRank(newGuildRank);

        // Save data
        PlayerData.get().save(promotee);

        // Call Event
        PlayerGuildRankChangeEvent playerGuildRankChangeEvent = new PlayerGuildRankChangeEvent(promotee, newGuildRank);
        playerGuildRankChangeEvent.run();

        // Inform promotee
        promotee.sendNotifyMsg(Messages.getMsg("commands.promote.promotee promoted msg", newGuildRank));

        // Inform promoter
        promoter.sendSuccessMsg(Messages.getMsg("commands.promote.successfully promoted", promotee, newGuildRank));
    }
}
