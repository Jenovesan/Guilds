package com.guildwars.guildwars.guilds.cmd.req;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class InMainWorldReq extends Req {
    @Override
    public void check(GPlayer player) throws CmdException {
        if (!gUtil.isInMainWorld(player)) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.not in main world"));
    }
}
