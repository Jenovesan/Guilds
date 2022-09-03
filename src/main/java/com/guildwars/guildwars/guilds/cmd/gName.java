package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;

public class gName extends gCommand{

    public gName() {
        super("name");
        this.setMinArgs(1);
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.SET_NAME, true)) {
            return;
        }

        String newGuildName = args[0];
        // Command return messages are handled in this method
        if (!gUtil.guildNameLegal(player, newGuildName)) {
            return;
        }

        // Set Guild name
        Guild guild = player.getGuild();
        String oldGuildName = guild.getName();
        guild.setName(newGuildName);

        // Save data
        GuildData.get().save(guild);

        // Update index
        GuildsIndex.get().updateName(guild, oldGuildName, newGuildName);

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.name changed", player, newGuildName));

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.name.successfully set name", args[0]));
    }
}
