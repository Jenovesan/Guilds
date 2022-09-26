package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;

public class gPower extends gCommand{

    public gPower() {
        // Name
        super("power");

        // Aliases
        addAlias("pow");
    }

    @Override
    public void perform() {
        // Args
        float playerPower = gPlayer.getPower();
        int playerMaxPower = gPlayer.getMaxPower();
        String powerMsg = Messages.get(Plugin.GUILDS).get("commands.power.player power", playerPower + "/" + playerMaxPower);

        // Send gPlayer their guild's power
        // Only do this if they are in a guild
        if (gPlayer.isInGuild()) {
            // Args
            int guildPower = guild.getPower();
            int maxGuildPower = guild.getMaxPower();

            // Prepare
            powerMsg = powerMsg.concat("\n" + Messages.get(Plugin.GUILDS).get("commands.power.guild power", guildPower + "/" + maxGuildPower));
        }

        // Apply

        gPlayer.sendNotifyMsg(powerMsg);
    }
}
