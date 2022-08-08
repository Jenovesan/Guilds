package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Power implements Listener {

    final int onlinePlayerPowerRegenTime = Config.get().getInt("online player power regen time (min)");
    final int offlinePlayerPowerRegenTime = Config.get().getInt("offline player power regen (min)");

    @EventHandler
    public void losePowerOnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        gPlayer gPlayer = gPlayers.get(player);

        int powerLossOnDeath = Config.get().getInt("power change on death");

        gPlayer.changePower(powerLossOnDeath);

        regenPower(gPlayer, player, 60);
    }

    public void regenPower(gPlayer gPlayer, Player player, int powerTimer) {
        int taskWaitTime = ((player.isOnline() ? onlinePlayerPowerRegenTime : offlinePlayerPowerRegenTime) / 60) * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (powerTimer == 0) {
                    gPlayer.changePower(1);
                    if (gPlayer.getPower() == Config.get().getInt("player max power")) {
                        return;
                    }
                    regenPower(gPlayer, player, 60);
                } else {
                    regenPower(gPlayer, player, powerTimer - 1);
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), taskWaitTime);
    }


}
