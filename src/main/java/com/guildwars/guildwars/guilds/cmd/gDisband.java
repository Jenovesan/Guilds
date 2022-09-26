package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.cmd.req.GuildLeaderReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gDisband extends gCommand {

    public gDisband() {
        // Name
        super("disband");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildLeaderReq());

        // Set async because many functions will run to clear the guild's data
        setAsync(true);
    }

    @Override
    public void perform() {
        // Apply
        guild.disband();

        // Inform

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.disband"));

        // Inform disbander
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.disband.success", gPlayer.describe(guild)));
    }
}
