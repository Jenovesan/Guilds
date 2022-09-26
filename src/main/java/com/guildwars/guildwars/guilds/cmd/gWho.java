package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.entity.Guild;

public class gWho extends gCommand{

    public gWho() {
        super("who");
    }

    @Override
    public void perform() {
        Guild guild = gPlayer.getGuild();
        if (guild != null) {
            gPlayer.sendMessage(guild.getName() + guild.getDescription());
        } else {
            gPlayer.sendMessage("Player is not in a guild");
        }
    }
}
