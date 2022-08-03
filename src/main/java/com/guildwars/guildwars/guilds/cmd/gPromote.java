package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import com.guildwars.guildwars.utils.util;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

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
            promoter.sendFailMsg(Messages.getMsg("commands.promote.not in guild"));
            return;
        }

        Guild guild = promoter.getGuild();

        OfflinePlayer promotee = guild.getOfflinePlayer(args[0]);

        if (promotee == null) {
            promoter.sendFailMsg(Messages.getMsg("commands.promote.promotee not found").replace("<input>", args[0]));
            return;
        }

        GuildRank promoterGuildRank = promoter.getGuildRank();
        GuildRank promoteeGuildRank = guild.getGuildRank(promotee);
        if (GuildRank.higherByAmount(promoterGuildRank, promoteeGuildRank) < 2) {
            if (promoterGuildRank != GuildRank.LEADER) {
                promoter.sendFailMsg(Messages.getMsg("commands.promote.rank not high enough").replace("<name>", Objects.requireNonNullElse(promotee.getName(), "")));
            } else { // Tried to promote member to GuildRank: Leader
                promoter.sendFailMsg(Messages.getMsg("commands.promote.tried to make leader"));
            }
            return;
        }

        // Promote promotee
        GuildRank newGuildRank = GuildRank.getGuildRankByLevel(promoteeGuildRank.level + 1);
        guild.changeGuildRank(promotee, newGuildRank);

        // Update gPlayer & Inform promotee if online
        Player promoteePlayer = promotee.getPlayer();
        if (promoteePlayer != null) {
            // Update gPlayer
            gPlayer promoteeGPlayer = gPlayers.get(promoteePlayer);
            promoteeGPlayer.setGuildRank(newGuildRank);

            // Inform
            pUtil.sendSuccessMsg(promoteePlayer, Messages.getMsg("commands.promote.promotee promoted msg").replace("<rank>", util.formatEnum(newGuildRank)));
        }

        // Inform promoter
        promoter.sendSuccessMsg(Messages.getMsg("commands.promote.successfully promoted").replace("<name>", Objects.requireNonNullElse(promotee.getName(), "")).replace("<rank>", util.formatEnum(newGuildRank)));
    }
}