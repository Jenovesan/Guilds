package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.cmd.arg.GuildDescArg;
import com.guildwars.guildwars.guilds.cmd.arg.GuildNameArg;
import com.guildwars.guildwars.guilds.cmd.req.NotInGuildReq;
import com.guildwars.guildwars.guilds.event.GuildCreatedEvent;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;

public class gCreate extends gCommand {

    public gCreate() {
        // Name
        super("create");

        // Aliases
        addAlias("new");
        addAlias("form");

        // Reqs
        addReq(new NotInGuildReq());

        // Args
        addArg(new GuildNameArg(true));
        addArg(new GuildDescArg(false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        String guildName = readNextArg();
        String guildDesc = readNextArg("None");

        // Apply

        // Create new Guild
        Guild newGuild = new Guild(guildName, guildDesc);

        // Call event
        GuildCreatedEvent guildCreatedEvent = new GuildCreatedEvent(newGuild);
        guildCreatedEvent.run();

        // Update gPlayer
        gPlayer.createdGuild(newGuild);

        // Call Event
        GPlayerGuildChangedEvent gPlayerGuildChangedEvent = new GPlayerGuildChangedEvent(gPlayer, null, newGuild, GPlayerGuildChangedEvent.Reason.CREATION);
        gPlayerGuildChangedEvent.run();

        // Inform
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.create.success", gPlayer.describe(newGuild)));
    }
}
