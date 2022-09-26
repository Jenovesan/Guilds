package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.BoardMap;
import com.guildwars.guildwars.guilds.cmd.arg.BoolArg;

public class gMap extends gCommand {

    public gMap() {
        // Name
        super("map", "automap");

        // Aliases
        addAlias("mapauto");

        // Args
        addArg(new BoolArg(false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        Boolean enableMapAuto = readNextArg();

        // Player did not try to enable nor disable map-auto-update
        if (getCmd().equalsIgnoreCase("map") && enableMapAuto == null) {

            // Prepare

            BoardMap map = new BoardMap(gPlayer);

            // Apply

            map.sendMap();
        }
        // Player tried to enable or disable map-auto-update
        else {

            // Apply

            gPlayer.setAutoMapping(enableMapAuto);

            // Inform
            System.out.println("map.auto " + enableMapAuto);
            gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.map.auto " + enableMapAuto));
        }
    }
}