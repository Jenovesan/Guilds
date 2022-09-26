package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.guilds.Guilds;

import java.util.List;

public class gList extends gCommand {
    public gList() {
        // Name
        super("list");
    }

    @Override
    void perform() {
        String fList = Messages.get(Plugin.GUILDS).get("commands.list.list construction.header") + "\n";
        
        List<Guild> onlineGuilds = Guilds.get().getAll().stream().filter(Guild::hasOnlinePlayer).toList();

        for (Guild guild : onlineGuilds) {
            int onlinePlayers = guild.getNumberOfOnlinePlayers();
            int playerCount = guild.getPlayerCount();
            int claims = guild.getNumberOfClaims();
            int power = guild.getPower();
            int maxPower = guild.getMaxPower();
            fList = fList.concat(Messages.get(Plugin.GUILDS).get("commands.list.list construction.data", guild, onlinePlayers, playerCount, claims, power, maxPower)) + "\n";
        }

        fList = fList.concat(Messages.get(Plugin.GUILDS).get("commands.list.list construction.footer"));

        player.sendMessage(fList);
    }
}
