package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.cmd.CmdException;

public class IntArg extends Arg<Integer> {

    public IntArg(boolean required) {
        super(required, "amount");
    }

    @Override
    public Integer read(String arg, GPlayer gPlayer) throws CmdException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.not a number"));
        }
    }
}
