package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;

public abstract class gCommand {

    final String cmdName;
    String description;
    String usage;
    Integer minArgs = 0;

    public gCommand(String cmdName) {
        this.cmdName = cmdName;
        this.description = Messages.getMsg("commands." + this.cmdName + ".description");
        this.usage = Messages.getMsg("commands." + this.cmdName + ".usage");
    }

    public String getDescription() {
        return description;
    };

    public String getUsage() {
        return usage;
    };

    public Integer getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int args) {
        minArgs = args;
    }

    public abstract void perform(gPlayer player, String args[]);
}
