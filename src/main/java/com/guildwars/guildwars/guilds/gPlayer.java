package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class gPlayer {

    private final Player player;
    private final UUID uuid;
    private Guild guild;
    private int guildId = -1;
    private GuildRank guildRank;
    private final String name;

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

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setGuildId(int id) {
        this.guildId = id;
    }

    public void setGuildRank(GuildRank rank) {
        this.guildRank = rank;
    }

    public gPlayer (Player player, Guild guild) {
        this.player = player;
        UUID uuid = player.getUniqueId();
        this.uuid = uuid;
        this.guild = guild;
        this.name = player.getName();
        if (guild != null) {
            this.guildId = guild.getId();
            this.guildRank = guild.getPlayers().get(uuid);
        }
    }

    public void sendMessage(String msg) {
        getPlayer().sendMessage(msg);
    }

    public void sendSuccessMsg(String msg) {
        pUtil.sendSuccessMsg(this.getPlayer(), msg);
    }

    public void sendFailMsg(String msg) {
        pUtil.sendFailMsg(this.getPlayer(), msg);
    }

    public Boolean isInGuild() { return getGuildId() != -1; }

    public void joinedNewGuild(Guild newGuild) {
        this.setGuild(newGuild);
        this.setGuildId(newGuild.getId());
        this.setGuildRank(newGuild.getRank(this.getUUID()));
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildId(-1);
        this.setGuildRank(null);
    }
}
