package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class gPlayer {

    private Player player = null;
    private final UUID uuid;
    private Guild guild;
    private int guildId = 0;
    private GuildRank guildRank;
    private String name;
    private int power = Objects.requireNonNullElse(PlayerData.get().getInt("power." + this.getUUID()), Config.get().getInt("player max power"));

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public int getGuildId() {
        return this.guildId;
    }

    public GuildRank getGuildRank() {
        return this.guildRank;
    }

    public String getName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setGuildId(int id) {
        this.guildId = id;
    }

    public void setGuildRank(GuildRank rank) {
        this.guildRank = rank;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void changePower(int changeBy) {
        int playerMaxPower = Config.get().getInt("player max power");
        int playerMinPower = Config.get().getInt("player min power");
        this.power = Math.min(Math.max(this.power + changeBy, playerMinPower), playerMaxPower);
    }

    // For loading gPlayer from data
    public gPlayer(UUID uuid, int guildId, GuildRank guildRank, String name, int power) {
        this.uuid = uuid;
        this.guild = guild;
        this.guildId = guildId;
        this.guildRank = guildRank;
        this.name = name;
        this.power = power;
    }

    // For creating a gPlayer for the first time
    public gPlayer (Player player) {
        this.player = player;
        UUID uuid = player.getUniqueId();
        this.uuid = uuid;
        this.name = player.getName();
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

    public Boolean isInGuild() { return getGuild() != null; }

    public void joinedNewGuild(Guild newGuild) {
        this.setGuild(newGuild);
        this.setGuildId(newGuild.getId());
        this.setGuildRank(newGuild.getRank(this));
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildId(-1);
        this.setGuildRank(null);
    }
}
