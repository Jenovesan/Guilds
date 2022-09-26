package com.guildwars.guildwars.guilds.cmd.req;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.utils.util;

public class GuildLeaderReq extends Req {

    @Override
    public void check(GPlayer player) throws CmdException {
        if (player.getGuildRank() != GuildRank.LEADER) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.guild rank too low", util.formatEnum(GuildRank.LEADER)));
    }
}
