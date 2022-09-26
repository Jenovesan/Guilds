package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Indexing;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.entity.GPlayer;

public class GPlayerArg extends Arg<GPlayer>{

    private final boolean GPlayerCanBeSender;

    public GPlayerArg(boolean required, boolean GPlayerCanBeSender) {
        super(required, "player");
        this.GPlayerCanBeSender = GPlayerCanBeSender;
    }

    @Override
    public GPlayer read(String arg, GPlayer sender) throws CmdException {
        GPlayer gPlayer = Indexing.get().getGPlayerByName(arg);

        if (gPlayer == null) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player not found", arg));
        System.out.println(gPlayer == sender);
        if (!GPlayerCanBeSender && gPlayer == sender) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.cannot target self"));
        return gPlayer;
    }
}
