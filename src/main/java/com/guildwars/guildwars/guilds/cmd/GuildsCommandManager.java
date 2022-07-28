package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildsCommandManager implements CommandExecutor {

    public gHelp helpCommand = new gHelp();

    public Map<String, gCommand> gCommands = Map.of(
        "create", new gCreate(),
        "disband", new gDisband(),
        "who", new gWho()
    );

    Set<String> gCommandNames = gCommands.keySet();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
                helpCommand.perform(sender, gCommands.values());
                return true;
            }

            String gCommandName = args[0].toLowerCase();

            if (!gCommandNames.contains(gCommandName)) {
                sender.sendMessage(Messages.getMsg("commands.command does not exist"));
                return true;
            }

            getgCommands().get(gCommandName).perform(player, args);
            return true;
        }



        return true;
    }

    public Map<String, gCommand> getgCommands() {
        return gCommands;
    }
}
























