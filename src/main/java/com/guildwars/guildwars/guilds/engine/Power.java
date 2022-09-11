package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gPlayers;
import com.guildwars.guildwars.guilds.gPlayersIndex;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Power extends Engine{

    private final float ONLINE_PLAYER_POWER_REGEN = Config.get(Plugin.GUILDS).getFloat("online player power regen per min");
    private final float OFFLINE_PLAYER_POWER_REGEN = Config.get(Plugin.GUILDS).getFloat("offline player power regen per min");

    public Power() {
        super(1200L);
        updatePower();
    }

    private final int MAX_POWER = Config.get(Plugin.GUILDS).getInt("player max power");

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
                PlayerData.get().save(player);
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

        int powerLossOnDeath = Config.get(Plugin.GUILDS).getInt("power change on death");

        gPlayer.changePower(powerLossOnDeath);

        // Call Event
        PlayerLosePowerEvent playerLosePowerEvent = new PlayerLosePowerEvent(gPlayer, gKiller);
        playerLosePowerEvent.run();
    }
}
