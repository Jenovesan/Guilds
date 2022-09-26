package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.*;

public class gCommandBase implements CommandExecutor {

    public static gCommandBase i = new gCommandBase();
    public static gCommandBase get() {
        return i;
    }

    private final gCommand[] commands = new gCommand[] {
            new gHelp(),
            new gCreate(),
            new gDisband(),
            new gWho(),
            new gInvite(),
            new gJoin(),
            new gDeInvite(),
            new gLeave(),
            new gKick(),
            new  gPromote(),
            new gDemote(),
            new gDesc(),
            new gName(),
            new gChat(),
            new gLeader(),
            new gClaim(),
            new gMap(),
            new gPower(),
            new gUnclaimAll(),
            new gAutoClaim(),
            new gSetHome(),
            new gHome(),
            new gList(),
            new gSetRelation()
    };

    public gCommand[] getCommands() {
        return commands;
    }

    private HashMap<String, gCommand> commandsIndex = new HashMap<>();

    public gCommandBase() {
        for (gCommand command : commands) {
            command.getNames().forEach(name -> commandsIndex.put(name, command));
            command.getAliases().forEach(alias -> commandsIndex.put(alias, command));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String inputedCmd = args.length > 0 ? args[0] : "help";
        gCommand cmd;

        if (commandsIndex.containsKey(inputedCmd)) {
            cmd = commandsIndex.get(inputedCmd);
        } else {
            sender.sendMessage(Messages.get(Plugin.GUILDS).get("commands.command does not exist")); // make fail message using a class
            return true;
        }

        cmd.perform(sender, inputedCmd, Arrays.copyOfRange(args, 1, args.length));

        return true;
    }
}
























