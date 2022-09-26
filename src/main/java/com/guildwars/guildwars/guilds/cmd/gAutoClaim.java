package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.cmd.arg.BoolArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gAutoClaim extends gCommand {

    public gAutoClaim() {
        // Name
        super("autoclaim");

        // Aliases
        addAlias("aclaim");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.CLAIM));

        // Args
        addArg(new BoolArg(false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        Boolean enable = readNextArg();

        // Prepare

        // Establish whether autoclaiming will be enabled or disabled
        if (enable == null) enable = !gPlayer.isAutoClaiming();

        // Apply
        gPlayer.setAutoClaiming(enable);

        // Inform
        gPlayer.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.autoclaim." + enable));
    }
}
