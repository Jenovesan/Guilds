package com.guildwars.guildwars.guilds.cmd.req;

import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.guilds.entity.GPlayer;

public abstract class Req {
    public abstract void check(GPlayer player) throws CmdException;
}
