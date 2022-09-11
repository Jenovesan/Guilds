package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildsCommandManager implements CommandExecutor {

    private final gHelp helpCommand = new gHelp();
    private final gCreate createCommand = new gCreate();
    private final gDisband disbandComand = new gDisband();
    private final gWho whoCommand = new gWho();
    private final gInvite inviteCommand = new gInvite();
    private final gJoin joinCommand = new gJoin();
    private final gDeInvite deInviteCommand = new gDeInvite();
    private final gLeave leaveCommand = new gLeave();
    private final gKick kickCommand = new gKick();
    private final gPromote promoteCommand = new gPromote();
    private final gDemote demoteCommand = new gDemote();
    private final gDesc descCommand = new gDesc();
    private final gName nameCommand = new gName();
    private final gChat chatCommand = new gChat();
    private final gEnemy enemyCommand = new gEnemy();
    private final gTruce truceCommand = new gTruce();
    private final gLeader leaderCommand = new gLeader();
    private final gClaim claimCommand = new gClaim();
    private final gMap mapCommand = new gMap();
    private final gPower powerCommand = new gPower();
    private final gUnclaim unclaimCommand = new gUnclaim();
    private final gUnclaimAll unclaimAllCommand = new gUnclaimAll();
    private final gAutoClaim autoClaimCommand = new gAutoClaim();
    private final gSetHome setHomeCommand = new gSetHome();
    private final gHome homeCommand = new gHome();


    public Map<String, gCommand> gCommands = Map.ofEntries(
        Map.entry("create", createCommand),
        Map.entry("new", createCommand),
        Map.entry("disband", disbandComand),
        Map.entry("who", whoCommand),
        Map.entry("show", whoCommand),
        Map.entry("invite", inviteCommand),
        Map.entry("join", joinCommand),
        Map.entry("deinvite", deInviteCommand),
        Map.entry( "leave", leaveCommand),
        Map.entry("kick", kickCommand),
        Map.entry("promote", promoteCommand),
        Map.entry("demote", demoteCommand),
        Map.entry("desc", descCommand),
        Map.entry("setdesc", descCommand),
        Map.entry("name", nameCommand),
        Map.entry("setname", nameCommand),
        Map.entry("chat", chatCommand),
        Map.entry("c", chatCommand),
        Map.entry("enemy", enemyCommand),
        Map.entry("truce", truceCommand),
        Map.entry("leader", leaderCommand),
        Map.entry("owner", leaderCommand),
        Map.entry("claim", claimCommand),
        Map.entry("map", mapCommand),
        Map.entry("power", powerCommand),
        Map.entry("unclaim", unclaimCommand),
        Map.entry("unclaimall", unclaimAllCommand),
        Map.entry("autoclaim", autoClaimCommand),
        Map.entry("sethome", setHomeCommand),
        Map.entry("home", homeCommand)
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
                sender.sendMessage(Messages.get(Plugin.GUILDS).get("commands.command does not exist"));
                return true;
            }

            gCommand guildCommand = getgCommands().get(gCommandName);

            if (modifiedArgs.length < guildCommand.getMinArgs()) {
                sender.sendMessage(Messages.get(Plugin.GUILDS).get("commands.too few arguments given"));
                return true;
            }
            gPlayer gPlayer = gPlayersIndex.get().getByPlayer(player);

            guildCommand.perform(gPlayer, modifiedArgs);
            return true;
        }



        return true;
    }

    public Map<String, gCommand> getgCommands() {
        return gCommands;
    }
}
























