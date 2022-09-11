package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.gPlayer;

public class gWho extends gCommand{

    public gWho() {
        super("who");
    }

    @Override
    public String getDescription() {
        return Messages.get(Plugin.GUILDS).get("commands.who.description");
    }

    @Override
    public String getUsage() {
        return Messages.get(Plugin.GUILDS).get("commands.who.usage");
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
