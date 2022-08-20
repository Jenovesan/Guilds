package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Power implements Listener {

    private static HashMap<gPlayer, Float> playerPowerRegenTimes = new HashMap<>();

    private static final int onlinePlayerPowerRegenTime = Config.get().getInt("online player power regen time (min)");
    private static final int offlinePlayerPowerRegenTime = Config.get().getInt("offline player power regen (min)");

    private final static float defaultPlayerRegenTime = 60;

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int maxPower = Config.get().getInt("player max power");
                long currentTime = System.currentTimeMillis();
                for (Map.Entry<gPlayer, Float> entry : playerPowerRegenTimes.entrySet()) {
                    gPlayer player = entry.getKey();
                    float timeLeftToRegenPower = entry.getValue();
                    if (player.isOnline()) {
                        timeLeftToRegenPower -= 60F / onlinePlayerPowerRegenTime;
                    } else {
                        timeLeftToRegenPower -= 60F / offlinePlayerPowerRegenTime;
                    }


                    if (timeLeftToRegenPower > 0.01) {
                        playerPowerRegenTimes.put(player, timeLeftToRegenPower);
                    } else {
                        player.changePower(Config.get().getInt("player power regen amount"));
                        player.setPowerChangeTime(currentTime); // Still have to update this value encase the admin changes max power in config
                        // Player is at max power.
                        // They do not need to regenerate power.
                        if (player.getPower() >= maxPower) {
                            playerPowerRegenTimes.remove(player);
                        }
                        // Player is below max power.
                        // They need to regenerate more power.
                        else {
                            player.setPowerChangeTime(currentTime);
                            playerPowerRegenTimes.replace(player, defaultPlayerRegenTime);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 1200, 1200);
    }

    public static void load() {
        int maxPower = Config.get().getInt("player max power");
        long currentTime = System.currentTimeMillis();
        for (gPlayer player : gPlayers.getAllGPlayers()) {
            // Player is at max power
            if (player.getPower() == maxPower) {
                continue;
            } else if (player.getPower() > maxPower) {
                player.setPower(maxPower);
                continue;
            }
            // Player is below max power
            float timeSincePowerChange = (float) ((currentTime - player.getPowerChangeTime()) / 60000); // In minutes

            playerPowerRegenTimes.put(player, timeSincePowerChange);

        }
    }


    @EventHandler
    public void losePowerOnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();

        if (player == null) return;

        gPlayer gPlayer = gPlayers.get(player);
        gPlayer gKiller = killer != null ? gPlayers.get(killer) : null;

        int powerLossOnDeath = Config.get().getInt("power change on death");

        gPlayer.changePower(powerLossOnDeath);
        gPlayer.setPowerChangeTime(System.currentTimeMillis());

        // Call Event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerLosePowerEvent(gPlayer, gKiller));

        playerPowerRegenTimes.putIfAbsent(gPlayer, defaultPlayerRegenTime);
    }
}
