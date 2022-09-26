package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Guild;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GuildsList {

    private void sendTo(GPlayer player) {
        new BukkitRunnable() {
            @Override
            public void run() {
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
        }.runTaskAsynchronously(GuildWars.getInstance());
    }
}
