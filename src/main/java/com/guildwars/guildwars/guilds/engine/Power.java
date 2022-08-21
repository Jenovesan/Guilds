package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class Power implements Listener {

    private static HashSet<gPlayer> players = new HashSet<>();

    private static HashSet<gPlayer> getPlayers() {
        return players;
    }

    private static final int onlinePlayerPowerRegenAmount = Config.get().getInt("online player power regen per min");
    private static final int offlinePlayerPowerRegenAmount = Config.get().getInt("offline player power regen per min");

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int maxPower = Config.get().getInt("player max power");
                for (gPlayer player : players) {
                    if (player.isOnline()) {
                        player.changePower(onlinePlayerPowerRegenAmount);
                    } else {
                        player.changePower(offlinePlayerPowerRegenAmount);
                    }

                    if (player.getPower() >= maxPower) {
                        players.remove(player);
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 1200, 1200);
    }

    public static void load() {
        int maxPower = Config.get().getInt("player max power");
        for (gPlayer player : gPlayers.getgInstance().getAll()) {
            // Player is at max power
            if (player.getPower() == maxPower) continue;

            if (player.getPower() > maxPower) {
                player.setPower(maxPower);
                continue;
            }
            // Player power is below max
            getPlayers().add(player);
        }
    }

    @EventHandler
    public void losePowerOnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();

        if (player == null) return;

        gPlayer gPlayer = gPlayersIndex.get().getByPlayer(player);
        gPlayer gKiller = killer != null ? gPlayersIndex.get().getByPlayer(killer) : null;

        int powerLossOnDeath = Config.get().getInt("power change on death");

        gPlayer.changePower(powerLossOnDeath);

        // Call Event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerLosePowerEvent(gPlayer, gKiller));

        getPlayers().add(gPlayer);
    }
}
