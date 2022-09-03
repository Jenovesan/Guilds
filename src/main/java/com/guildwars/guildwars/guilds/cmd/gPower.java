package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;

public class gPower extends gCommand{

    public gPower() {
        super("power");
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        // Check power
        int guildPower = player.getGuild().getPower();
        int maxGuildPower = player.getGuild().getMaxPower();

        player.sendNotifyMsg(Messages.getMsg("commands.power.power msg", guildPower + "/" + maxGuildPower));
    }
}
