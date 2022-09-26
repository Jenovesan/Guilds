package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.Indexing;
import com.guildwars.guildwars.guilds.cmd.arg.Arg;
import com.guildwars.guildwars.guilds.cmd.req.Req;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class gCommand {

    private final List<String> names = new ArrayList<>();
    private final List<String> descriptions = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    protected String cmd;

    private String[] inputs;
    private final List<Req> requirements = new ArrayList<>();
    private final List<Arg> arguments = new ArrayList<>();
    private int index = 0;

    protected GPlayer gPlayer;
    protected Player player;
    protected Guild guild;

    private boolean async;

    public gCommand(String... names) {
        for (String name : names) {
            this.names.add(name);
            this.descriptions.add(Messages.get(Plugin.GUILDS).get("commands." + name + ".description"));
        }
    }
    
    public List<String> getDescriptions() {
        return names.stream().map(name -> Messages.get(Plugin.GUILDS).get("commands." + name + ".description")).collect(Collectors.toList());
    }

    public List<String> getUsages() {
        List<String> usageMsgs = new ArrayList<>();
        for (String name : names) {
            String usageMsg = "/g " + name;
            
            for (Arg<?> arg : arguments) {
                usageMsg = usageMsg.concat(
                        (arg.isRequired() ? " <" : " <?")
                        + arg.asString()
                        + (arg.isRequired() ? "> " : "?>"));
            }
            
            usageMsgs.add(usageMsg);
        }
        return usageMsgs;
    }
    
    
    public String getCmd() {
        return cmd;
    }

    public void addReq(Req req) {
        requirements.add(req);
    }

    public List<String> getNames() {
        return names;
    }

    public void addArg(Arg<?> arg) {
        arguments.add(arg);
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public List<String> getAliases() {
        return aliases;
    }

    private boolean remainingInputs() {
        return index < inputs.length;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @SuppressWarnings("unchecked")
    public <T> T readNextArg(T defaultTo) throws CmdException {
        Arg<T> arg = arguments.get(index);
        T ret;
        // Check if there is a remaining input
        if (!remainingInputs()) {
            // Arg is required but does not exist, throw not enough arguments exception
            if (arg.isRequired()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.not enough arguments"));
            // Arg is not required, so just return null
            else ret = defaultTo;
        }
        // There is a remaining input
        else if (!arg.isVarargs()) ret = arg.read(cleanInput(inputs[index]), gPlayer);
        else {
            List<T> argReturns = new ArrayList<>();
            for (int i = index; i < inputs.length; i++) {
                argReturns.add(arg.read(cleanInput(inputs[i]), gPlayer));
            }
            ret = (T) argReturns.stream().map(T::toString).collect(Collectors.joining(" "));
        }
        index++;
        return ret;
    }

    private String cleanInput(String input) {
        return input.replace("\\", "");
    }

    public <T> T readNextArg() throws CmdException {
        return readNextArg(null);
    }

    public void perform(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player player) {

            GPlayer gPlayer = Indexing.get().getGPlayerByUUID(player.getUniqueId());

            this.gPlayer = gPlayer;
            this.guild = gPlayer.getGuild();

            try {
                // Command requirements
                for (Req requirement : requirements) {
                    requirement.check(gPlayer);
                }

                // Pass checks, now run the command
                this.inputs = args;
                this.cmd = cmd;
                this.player = player;
                index = 0;

                // Perform sync
                if (!async) {
                    perform();
                }
                // Perform async
                else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                perform();
                            } catch (CmdException e) {
                                handleCmdException(e);
                            }
                        }
                    }.runTaskAsynchronously(GuildWars.getInstance());
                }

            } catch (CmdException exception) {
                handleCmdException(exception);
            }
        }
    }

    private void handleCmdException(CmdException exception) {
        gPlayer.sendFailMsg(exception.getReason());
    }

    abstract void perform() throws CmdException;
}
