package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;

public class gUnclaimAll extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.unclaimall.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.unclaimall.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.UNCLAIM_ALL, true)) {
            return;
        }

        // Unclaim all
        Guild guild = player.getGuild();

        // Update Board
        for (int[] claimBoardLocation : guild.getClaimLocations()) {
            Board.getBoard()[claimBoardLocation[0]][claimBoardLocation[1]].setWilderness();
        }

        // Update guild
        guild.unclaimAll();

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.unclaimed all", player));

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.unclaimall.successfully unclaimed all"));
    }
}
