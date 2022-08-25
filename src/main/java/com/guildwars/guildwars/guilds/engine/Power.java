package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Power extends Engine{

    private final int ONLINE_PLAYER_POWER_REGEN = Config.get().getInt("online player power regen per min");
    private final int OFFLINE_PLAYER_POWER_REGEN = Config.get().getInt("offline player power regen per min");

    public Power() {
        super(1200L);
        updatePower();
    }

    private final int MAX_POWER = Config.get().getInt("player max power");

    @Override
    public void run() {
        for (gPlayer player : gPlayers.get().getAll()) {

            if (player.getPower() == MAX_POWER) continue;

            if (player.isOnline()) {
                player.changePower(ONLINE_PLAYER_POWER_REGEN);
            } else {
                player.changePower(OFFLINE_PLAYER_POWER_REGEN);
            }
        }
    }

    public void updatePower() {
        for (gPlayer player : gPlayers.get().getAll()) {
            // Lower player power if above max power.
            // Will run if admin lowers max power
            if (player.getPower() > MAX_POWER) {
                player.setPower(MAX_POWER);
            }
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
        PlayerLosePowerEvent playerLosePowerEvent = new PlayerLosePowerEvent(gPlayer, gKiller);
        playerLosePowerEvent.run();
    }
}
