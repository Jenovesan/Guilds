package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.engine.MapAuto;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;

public class gMap extends gCommand {
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.map.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.map.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Board.getMap(player));
        } else if (args[0].equalsIgnoreCase("auto")){
            // Has gMap auto enabled, disable gMap auto
            if (player.isAutoMapping()) {
                player.setAutoMapping(false);
                player.sendSuccessMsg(Messages.getMsg("map auto.disabled"));
            }
            // Has gMap auto disabled, enable gMap auto
            else {
                player.setAutoMapping(true);
                player.sendSuccessMsg(Messages.getMsg("map auto.enabled"));
            }

        } else if (args[0].equalsIgnoreCase("on")) {
            player.setAutoMapping(true);
            player.sendSuccessMsg(Messages.getMsg("map auto.enabled"));
        } else if (args[0].equalsIgnoreCase("off")) {
            player.setAutoMapping(false);
            player.sendSuccessMsg(Messages.getMsg("map auto.disabled"));
        } else {
            player.sendFailMsg(Messages.getMsg("commands.map.invalid syntax"));
        }
    }
}