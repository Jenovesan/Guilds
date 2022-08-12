package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class gDesc extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.desc.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.desc.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.SET_DESC, true)) {
            return;
        }

        // Set Guild Description
        String description = String.join(" ", args);
        Guild guild = player.getGuild();
        guild.setDescription(player, description);

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.desc.successfully set desc", player, null, String.join(" ", args)));
    }
}
