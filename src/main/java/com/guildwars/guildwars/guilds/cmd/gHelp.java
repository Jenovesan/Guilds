package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;

import java.util.List;

public class gHelp extends gCommand {
    public gHelp() {
        // Name
        super("help");
    }

    @Override
    void perform() throws CmdException {
        // Args
        gCommand[] gCommands = gCommandBase.get().getCommands();
        String help_msg = Messages.get(Plugin.GUILDS).get("commands.help.title") + "\n";

        // Prepare

        for (gCommand cmd : gCommands) {
            List<String> descriptions = cmd.getDescriptions();
            List<String> usages = cmd.getUsages();

            for (int i = 0; i < descriptions.size(); i++) {
                help_msg = help_msg.concat(Messages.get(Plugin.GUILDS).get("commands.help.format",
                        usages.get(i), descriptions.get(i)) + "\n");
            }
        }

        // Apply

        player.sendMessage(help_msg);
    }
}
