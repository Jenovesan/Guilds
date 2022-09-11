package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.gPlayer;

public class gPower extends gCommand{

    public gPower() {
        super("power");
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
            return;
        }

        // Check power
        int guildPower = player.getGuild().getPower();
        int maxGuildPower = player.getGuild().getMaxPower();

        player.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("commands.power.power msg", guildPower + "/" + maxGuildPower));
    }
}
