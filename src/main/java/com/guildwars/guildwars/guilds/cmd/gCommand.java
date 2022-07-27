package com.guildwars.guildwars.guilds.cmd;

import org.bukkit.entity.Player;

public abstract class gCommand {

    public abstract String getDescription();

    public abstract String getUsage();

    public abstract void perform(Player player, String args[]);
}
