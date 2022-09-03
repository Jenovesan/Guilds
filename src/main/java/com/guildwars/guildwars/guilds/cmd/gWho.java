package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;

public class gWho extends gCommand{

    public gWho() {
        super("who");
    }

    @Override
    public String getDescription() {
        return Messages.getMsg("commands.who.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.who.usage");
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {
        Guild guild = gPlayer.getGuild();
        if (guild != null) {
            gPlayer.sendMessage(guild.getName() + guild.getDescription());
        } else {
            gPlayer.sendMessage("Player is not in a guild");
        }
    }
}
