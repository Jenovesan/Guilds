package com.guildwars.guildwars.core;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CorePlayer {

    private Player player;
    private final UUID uuid;
    private String name;

    public Player getPlayer() {
        return player;
    }

    public UUID getUUID() {
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
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    // For loading CorePlayer from file
    public CorePlayer(UUID uuid, String name) {
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

    public void teleport(float chargeUp, Location to) {
        Teleport teleport = new Teleport(player, chargeUp, to);
        teleport.teleport();
    }
}
