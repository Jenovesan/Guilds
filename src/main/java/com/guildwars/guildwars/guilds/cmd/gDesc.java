package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class gDesc extends gCommand{

    public gDesc() {
        super("desc");
        setMinArgs(1);
        mustBeInGuild(true);
        setMinPermission(GuildPermission.SET_DESC);
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
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
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.description changed", player, description));

        // Inform
        player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.desc.successfully set desc", String.join(" ", args)));
    }
}
