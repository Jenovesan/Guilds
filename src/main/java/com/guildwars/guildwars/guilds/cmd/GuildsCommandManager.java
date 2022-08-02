package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
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
        "who", new gWho(),
        "invite", new gInvite(),
        "join", new gJoin(),
        "deinvite", new gDeInvite(),
        "leave", new gLeave(),
        "kick", new gKick(),
        "promote", new gPromote(),
        "demote", new gDemote()
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
            gCommand guildCommand = getgCommands().get(gCommandName);
            String[] modifiedArgs = Arrays.copyOfRange(args, 1, args.length);

            if (modifiedArgs.length < guildCommand.getMinArgs()) {
                sender.sendMessage(Messages.getMsg("commands.too few arguments given"));
                return true;
            }

            gPlayer gPlayer = gPlayers.get(player);

            guildCommand.perform(gPlayer, modifiedArgs);
            return true;
        }



        return true;
    }

    public Map<String, gCommand> getgCommands() {
        return gCommands;
    }
}
























