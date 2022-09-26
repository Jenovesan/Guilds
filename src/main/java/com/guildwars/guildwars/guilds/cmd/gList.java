package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.cmd.arg.IntArg;
import com.guildwars.guildwars.guilds.entity.GuildsList;

public class gList extends gCommand {
    public gList() {
        // Name
        super("list");

        // Args
        addArg(new IntArg(false));
    }

    @Override
    void perform() throws CmdException {
        // Args
        int page = readNextArg(1);

        // Prepare

        GuildsList guildsList = new GuildsList(gPlayer);

        // Check if page is valid
        if (!guildsList.isPage(page)) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.list.page not exist"));

        // Apply
        player.sendMessage(guildsList.getPage(page));
    }
}
