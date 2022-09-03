package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.GuildData;
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
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.SET_DESC, true)) {
            return;
        }

        // Set Guild Description
        String description = String.join(" ", args);
        Guild guild = player.getGuild();
        guild.setDescription(description);

        // Save data
        GuildData.get().save(guild);

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.description changed", player, description));

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.desc.successfully set desc", String.join(" ", args)));
    }
}
