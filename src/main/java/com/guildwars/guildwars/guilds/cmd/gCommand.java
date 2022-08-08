package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.gPlayer;

public abstract class gCommand {

    public abstract String getDescription();

    public abstract String getUsage();

    public abstract int getMinArgs();

    public abstract void perform(gPlayer player, String args[]);
}
