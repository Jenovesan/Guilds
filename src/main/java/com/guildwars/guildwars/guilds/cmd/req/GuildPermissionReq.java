package com.guildwars.guildwars.guilds.cmd.req;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import com.guildwars.guildwars.utils.util;

public class GuildPermissionReq extends Req {

    private final GuildPermission permission;

    public GuildPermissionReq(GuildPermission permission) {
        this.permission = permission;
    }

    @Override
    public void check(GPlayer player) throws CmdException {
        if (!gUtil.checkPermission(player, permission)) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.guild rank too low", util.formatEnum(player.getGuild().getRankFor(permission))));
    }
}
