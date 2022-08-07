package com.guildwars.guildwars.guilds.cmd;

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

    public Map<String, gCommand> gCommands = Map.ofEntries(
        Map.entry("create", new gCreate()),
        Map.entry("new", new gCreate()),
        Map.entry("disband", new gDisband()),
        Map.entry("who", new gWho()),
        Map.entry("show", new gWho()),
        Map.entry("invite", new gInvite()),
        Map.entry("join", new gJoin()),
        Map.entry("deinvite", new gDeInvite()),
        Map.entry( "leave", new gLeave()),
        Map.entry("kick", new gKick()),
        Map.entry("promote", new gPromote()),
        Map.entry("demote", new gDemote()),
        Map.entry("desc", new gDesc()),
        Map.entry("setdesc", new gDesc()),
        Map.entry("name", new gName()),
        Map.entry("setname", new gName()),
        Map.entry("chat", new gChat()),
        Map.entry("enemy", new gEnemy()),
        Map.entry("truce", new gTruce()),
        Map.entry("leader", new gLeader()),
        Map.entry("owner", new gLeader()),
        Map.entry("claim", new gClaim()),
        Map.entry("map", new gMap())
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

            String gCommandName;
            String[] modifiedArgs;
            if (args.length > 1 && gCommandNames.contains(args[0] + " " + args[1])) {
                gCommandName = (args[0] + " " + args[1]).toLowerCase();
                modifiedArgs = Arrays.copyOfRange(args, 2, args.length);
            } else if (gCommandNames.contains(args[0])) {
                gCommandName = args[0].toLowerCase();
                modifiedArgs = Arrays.copyOfRange(args, 1, args.length);
            } else {
                sender.sendMessage(Messages.getMsg("commands.command does not exist"));
                return true;
            }

            gCommand guildCommand = getgCommands().get(gCommandName);

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
























