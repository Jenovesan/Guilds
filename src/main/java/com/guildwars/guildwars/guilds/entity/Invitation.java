package com.guildwars.guildwars.guilds.entity;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;

import java.util.UUID;

public class Invitation {

    private final UUID playerUUID;
    private final long creationTime;
    private final long expireTime;

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public Invitation(GPlayer gPlayer) {
        this.playerUUID = gPlayer.getUUID();
        this.creationTime = System.currentTimeMillis();
        this.expireTime = creationTime + Config.get(Plugin.GUILDS).getInt("invite expire time (s)") * 1200L;
    }
}
