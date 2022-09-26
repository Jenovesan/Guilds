package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.arg.GuildNameArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.event.GuildRenameEvent;

public class gName extends gCommand{

    public gName() {
        // Name
        super("name");

        // Aliases
        addAlias("rename");
        addAlias("newname");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.SET_NAME));

        // Args
        addArg(new GuildNameArg(true));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        String oldName = guild.getName();
        String newName = readNextArg();

        // Apply

        guild.setName(newName);

        // Call event
        GuildRenameEvent guildRenameEvent = new GuildRenameEvent(guild, oldName, newName);
        guildRenameEvent.run();

        // Inform

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.name changed", guild.describe(gPlayer), newName));

        // Inform namer
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.name.success", newName));
    }
}
