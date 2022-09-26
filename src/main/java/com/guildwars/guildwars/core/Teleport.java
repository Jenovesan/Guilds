package com.guildwars.guildwars.core;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Teleport {

    private final int FADE_IN = Config.get(Plugin.CORE).getInt("teleporting title.fadeIn");
    private final int STAY = Config.get(Plugin.CORE).getInt("teleporting title.stay");
    private final int FADE_OUT = Config.get(Plugin.CORE).getInt("teleporting title.fadeOut");
    private final int BAR_LEN = Config.get(Plugin.CORE).getInt("teleporting title.bar length");
    private final String BAR_CHARGED_PREFIX = Messages.get(Plugin.CORE).get("teleporting.title.bar charged prefix");
    private final String BAR_UNCHARGED_PREFIX = Messages.get(Plugin.CORE).get("teleporting.title.bar uncharged prefix");
    private final String BAR = Messages.get(Plugin.CORE).get("teleporting.title.bar");

    private final Player player;
    private final float chargeUp;
    private final Location to;
    private double startLocationX;
    private double startLocationZ;

    private final static HashMap<Player, Integer> teleportTasksIds = new HashMap<>();

    public Teleport(Player player, float chargeUp, Location to) {
        this.player = player;
        this.chargeUp = chargeUp;
        this.to = to;
    }

    public void teleport() {
        startLocationX = player.getLocation().getX();
        startLocationZ = player.getLocation().getZ();

        // Cancel player teleport if they start a new teleport
        if (teleportTasksIds.containsKey(player)) Bukkit.getScheduler().cancelTask(teleportTasksIds.get(player));

        // Inform
        player.sendMessage(Messages.get(Plugin.CORE).get("teleporting.message.teleporting"));

        BukkitTask teleportTask = new BukkitRunnable() {
            int d = 0;
            @Override
            public void run() {

                if (playerHasMoved()) {
                    // Inform
                    player.sendMessage(Messages.get(Plugin.CORE).get("teleporting.message.cancelled"));

                    // Stop the teleporting
                    this.cancel();

                    // Remove from teleportTasksIds
                    teleportTasksIds.remove(player);
                }

                // Charge-up is complete
                if (d >= chargeUp) {
                    // Done with charge-up
                    player.teleport(to);

                    // Cancel the teleport
                    this.cancel();

                    // Remove from teleportTasksIds
                    teleportTasksIds.remove(player);

                    // Inform
                    player.sendMessage(Messages.get(Plugin.CORE).get("teleporting.message.teleported"));
                }

                // Send title
                player.sendTitle(
                        Messages.get(Plugin.CORE).get("teleporting.title.title"),
                        getBar(d),
                        FADE_IN, STAY, FADE_OUT
                );

                d++;
            }
        }.runTaskTimer(GuildWars.getInstance(), 0, 1);

        // Store teleport task
        teleportTasksIds.put(player, teleportTask.getTaskId());
    }

    private String getBar(int d) {
        String bar = BAR_CHARGED_PREFIX;
        int currentChargedBars = Math.round((BAR_LEN * (d / chargeUp)));
        for (int j = 0; j < currentChargedBars; j++) {
            bar = bar.concat(BAR);
        }
        bar = bar.concat(BAR_UNCHARGED_PREFIX);
        for (int j = currentChargedBars; j < BAR_LEN; j++) {
            bar = bar.concat(BAR);
        }
        return bar;
    }

    private boolean playerHasMoved() {
        return player.getLocation().getX() != startLocationX || player.getLocation().getZ() != startLocationZ;
    }
}
