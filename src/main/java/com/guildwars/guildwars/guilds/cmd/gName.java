package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class gName extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.name.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.name.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player.getPlayer(), null, args, null, null, null, null));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.SET_NAME)) {
            return;
        }

        String newGuildName = args[0];
        // Command return messages are handled in this method
        if (!gUtil.guildNameLegal(player.getPlayer(), newGuildName)) {
            return;
        }

        // Set Guild name
        player.getGuild().setName(player.getPlayer(), newGuildName);

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.name.successfully set name", player.getPlayer(), null, args, player.getGuild(), player.getGuild(), player.getGuildRank(), null));
    }
}
