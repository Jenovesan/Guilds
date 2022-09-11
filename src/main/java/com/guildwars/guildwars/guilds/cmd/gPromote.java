package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildRankChangeEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;

public class gPromote extends gCommand{

    public gPromote() {
        super("promote");
        setMinArgs(1);
        mustBeInGuild(true);
    }

    @Override
    public void perform(gPlayer promoter, String[] args) {

        gPlayer promotee = gPlayersIndex.get().getByName(args[0]);

        if (promotee == null) {
            promoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not found", args[0]));
            return;
        }

        Guild guild = promoter.getGuild();

        if (promotee.getGuild() != guild) {
            promoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.player not in your guild", promotee));
            return;
        }

        GuildRank promoterGuildRank = promoter.getGuildRank();
        GuildRank promoteeGuildRank = promotee.getGuildRank();
        if (GuildRank.higherByAmount(promoterGuildRank, promoteeGuildRank) < 2) {
            if (promoterGuildRank != GuildRank.LEADER) {
                promoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.promote.rank not high enough", promotee));
            } else { // Tried to promote member to GuildRank: Leader
                promoter.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.promote.tried to make leader"));
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
        promotee.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.promote.promotee promoted msg", newGuildRank));

        // Inform promoter
        promoter.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.promote.successfully promoted", promotee, newGuildRank));
    }
}
