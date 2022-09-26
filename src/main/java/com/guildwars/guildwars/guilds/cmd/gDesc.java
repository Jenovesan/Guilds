package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.cmd.arg.GuildDescArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gDesc extends gCommand{

    public gDesc() {
        // Name
        super("desc");

        // Alliases
        addAlias("description");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.SET_DESC));

        // Args
        addArg(new GuildDescArg(true));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        String description = readNextArg();

        // Apply
        guild.setDescription(description);

        // Inform

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.description set", guild.describe(gPlayer), description));

        // Inform
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.desc.success", description));
    }
}
