package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

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
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.UNCLAIM_ALL)) {
            return;
        }

        // Unclaim all
        player.getGuild().unclaimAll(player);

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.unclaimall.successfully unclaimed all", player, null, null));
    }
}
