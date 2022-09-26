package com.guildwars.guildwars.guilds.cmd.req;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.entity.GPlayer;

public class GuildHasHome extends Req {
    @Override
    public void check(GPlayer player) throws CmdException {
        if (!player.getGuild().hasHome()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.no guild home"));
    }
}
