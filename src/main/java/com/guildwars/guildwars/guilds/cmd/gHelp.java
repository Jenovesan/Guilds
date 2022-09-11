package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class gHelp {
    public void perform(CommandSender sender, Collection<gCommand> gCommands) {
        String help_msg = constructHelpMsg(gCommands);
        sender.sendMessage(help_msg);
    }

    private String constructHelpMsg(Collection<gCommand> gCommands) {
        String help_command = Messages.get(Plugin.GUILDS).get("commands.help.title") + "\n";
        for (gCommand gCommand : gCommands) {
            help_command = help_command.concat(
                    Messages.get(Plugin.GUILDS).get("commands.help.color") +
                            ChatColor.stripColor(gCommand.getUsage()) +
                            " - " +
                            ChatColor.stripColor(gCommand.getDescription()) +
                            "\n");
        }
        return help_command;
    }
}
