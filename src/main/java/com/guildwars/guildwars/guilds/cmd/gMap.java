package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.BoardMap;
import com.guildwars.guildwars.guilds.gPlayer;

public class gMap extends gCommand {

    public gMap() {
        super("map");
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        if (args.length == 0) {
            BoardMap map = new BoardMap(player);
            map.sendMap();
        } else if (args[0].equalsIgnoreCase("auto")){
            // Has gMap auto enabled, disable gMap auto
            if (player.isAutoMapping()) {
                player.setAutoMapping(false);
                player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("map auto.disabled"));
            }
            // Has gMap auto disabled, enable gMap auto
            else {
                player.setAutoMapping(true);
                player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("map auto.enabled"));
            }

        } else if (args[0].equalsIgnoreCase("on")) {
            player.setAutoMapping(true);
            player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("map auto.enabled"));
        } else if (args[0].equalsIgnoreCase("off")) {
            player.setAutoMapping(false);
            player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("map auto.disabled"));
        } else {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.map.invalid syntax"));
        }
    }
}