package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.event.GuildNameChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.Bukkit;

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

        // Call event
        Bukkit.getServer().getPluginManager().callEvent(new GuildNameChangeEvent(guild, oldGuildName, newGuildName));

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.name changed", player, newGuildName));

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.name.successfully set name", args[0]));
    }
}
