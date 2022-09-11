package com.guildwars.guildwars.core;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CorePlayer {

    private Player player;
    private final String uuid;
    private String name;

    public Player getPlayer() {
        return player;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // For creating CorePlayer from player / new player
    public CorePlayer(Player player) {
        this.player = player;
        this.uuid = String.valueOf(player.getUniqueId());
        this.name = player.getName();
    }

    // For loading CorePlayer from file
    public CorePlayer(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public boolean isOnline() {
        return player != null;
    }

    public void sendMessage(String msg) {
        getPlayer().sendMessage(msg);
    }

    public void sendSuccessMsg(String msg) {
        if (this.player != null) {
            pUtil.sendSuccessMsg(this.getPlayer(), msg);
        }
    }

    public void sendFailMsg(String msg) {
        if (this.player != null) {
            pUtil.sendFailMsg(this.getPlayer(), msg);
        }
    }

    public void sendNotifyMsg(String msg) {
        if (this.player != null) {
            pUtil.sendNotifyMsg(this.getPlayer(), msg);
        }
    }

    int FADE_IN = GuildWars.getCoreFM().getConfFile().getInt("teleporting title.fadeIn");
    int STAY = GuildWars.getCoreFM().getConfFile().getInt("teleporting title.stay");
    int FADE_OUT = GuildWars.getCoreFM().getConfFile().getInt("teleporting title.fadeOut");
    int BAR_LEN = GuildWars.getCoreFM().getConfFile().getInt("teleporting title.bar length");
    String BAR_CHARGED_PREFIX = GuildWars.getCoreFM().getMsgFile().get("teleporting.title.bar charged prefix");
    String BAR_UNCHARGED_PREFIX = GuildWars.getCoreFM().getMsgFile().get("teleporting.title.bar uncharged prefix");
    String BAR = GuildWars.getCoreFM().getMsgFile().get("teleporting.title.bar");

    public void teleport(float chargeUp, Location to) {

        double startLocationX = getPlayer().getLocation().getX();
        double startLocationZ = getPlayer().getLocation().getZ();

        // Inform
        sendMessage(GuildWars.getCoreFM().getMsgFile().get("teleporting.message.teleporting"));

        new BukkitRunnable() {
            int d = 0;
            @Override
            public void run() {
                // Check if player has moved
                if (getPlayer().getLocation().getX() != startLocationX || getPlayer().getLocation().getZ() != startLocationZ) {
                    // Inform
                    sendMessage(GuildWars.getCoreFM().getMsgFile().get("teleporting.message.cancelled"));

                    this.cancel();
                }

                // Charge-up is complete
                if (d >= chargeUp) {
                    // Done with charge-up
                    player.teleport(to);

                    // Inform
                    sendMessage(GuildWars.getCoreFM().getMsgFile().get("teleporting.message.teleported"));

                    this.cancel();
                }

                // Construct bar
                String bar = BAR_CHARGED_PREFIX;
                int currentChargedBars = Math.round((BAR_LEN * (d / chargeUp)));
                for (int j = 0; j < currentChargedBars; j++) {
                    bar = bar.concat(BAR);
                }
                bar = bar.concat(BAR_UNCHARGED_PREFIX);
                for (int j = currentChargedBars; j < BAR_LEN; j++) {
                    bar = bar.concat(BAR);
                }

                // Send title
                getPlayer().sendTitle(
                    GuildWars.getCoreFM().getMsgFile().get("teleporting.title.title"),
                    bar,
                    FADE_IN, STAY, FADE_OUT
                );

                d++;
            }
        }.runTaskTimer(GuildWars.getInstance(), 0, 1);
    }
}
