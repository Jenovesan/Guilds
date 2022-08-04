package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.files.Config;
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
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.SET_NAME)) {
            return;
        }

        String newGuildName = args[0];

        if (newGuildName.toCharArray().length > Config.get().getInt("max characters in guild name")) {
            player.sendFailMsg(Messages.getMsg("commands.name.guild name too long"));
            return;
        }

        if (Guilds.guidlNameExists(newGuildName)) {
            player.sendFailMsg(Messages.getMsg("commands.name.guild name exists"));
            return;
        }

        // Set Guild name
        player.getGuild().setName(player.getName(), newGuildName);

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.name.successfully set name").replace("<name>", newGuildName));
    }
}
