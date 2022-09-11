package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class gCommand {

    private final String cmdName;
    private final String description;
    private final String usage;
    private int minArgs;
    private boolean mustBeInGuild;
    private GuildPermission minPermission;

    public gCommand(String cmdName) {
        this.cmdName = cmdName;
        this.description = Messages.get(Plugin.GUILDS).get("commands." + this.cmdName + ".description");
        this.usage = Messages.get(Plugin.GUILDS).get("commands." + this.cmdName + ".usage");
    }

    public String getDescription() {
        return description;
    };

    public String getUsage() {
        return usage;
    };

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public void mustBeInGuild(boolean mustBeInGuild) {
        this.mustBeInGuild = mustBeInGuild;
    }

    public void setMinPermission(GuildPermission minPermission) {
        this.minPermission = minPermission;
    }

    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player playerPlayer) {

            gPlayer player = gPlayersIndex.get().getByPlayer(playerPlayer);

            // Command checks

            if (args.length < minArgs) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.too few arguments given"));
                return;
            }

            if (mustBeInGuild && !player.isInGuild()) {
                player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
                return;
            }

            if (minPermission != null && !gUtil.checkPermission(player, GuildPermission.CHAT, true)) {
                return;
            }
            // Passes check, now run the command
            perform(player, args);
        }
    }

    abstract void perform(gPlayer player, String[] args);
}
