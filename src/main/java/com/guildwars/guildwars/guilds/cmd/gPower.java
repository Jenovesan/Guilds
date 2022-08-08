package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;

public class gPower extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.power.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.power.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {

    }
}
