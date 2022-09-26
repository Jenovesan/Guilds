package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.utils.fUtil;

import java.util.HashMap;

public class BoolArg extends Arg<Boolean> {

    private final HashMap<String, Boolean> strBooleans = fUtil.getHashMapFromFile(Config.get(Plugin.GUILDS), "command booleans");

    public BoolArg(boolean required) {
        super(required, "on/off");
        Config.get(Plugin.GUILDS).getStringList("command booleans.true").forEach(str -> strBooleans.put(str, true));
        Config.get(Plugin.GUILDS).getStringList("command booleans.false").forEach(str -> strBooleans.put(str, false));
    }

    @Override
    public Boolean read(String arg, GPlayer gPlayer) throws CmdException {
        arg = arg.toLowerCase();
        if (!strBooleans.containsKey(arg)) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.not a boolean", arg));
        else return strBooleans.get(arg.toLowerCase());
    }
}
