package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.entity.GPlayer;

public class GuildDescArg extends Arg<String>{
    public GuildDescArg(boolean required) {
        super(required, "desc", true);
    }

    @Override
    public String read(String arg, GPlayer gPlayer) {
        return arg;
    }
}
