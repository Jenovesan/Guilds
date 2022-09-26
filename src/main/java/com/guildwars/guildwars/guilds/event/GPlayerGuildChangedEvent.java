package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.entity.GPlayer;

public class GPlayerGuildChangedEvent extends GuildsEvent {
    private final GPlayer gPlayer;
    private final Guild oldGuild;
    private final Guild newGuild;
    private final Reason reason;

    public GPlayer getGPlayer() {
        return gPlayer;
    }

    public Guild getOldGuild() {
        return oldGuild;
    }

    public Guild getNewGuild() {
        return newGuild;
    }

    public Reason getReason() {
        return reason;
    }

    public GPlayerGuildChangedEvent(GPlayer player, Guild oldGuild, Guild newGuild, Reason reason) {
        this.gPlayer = player;
        this.oldGuild = oldGuild;
        this.newGuild = newGuild;
        this.reason = reason;
    }

    public enum Reason {
        CREATION,
        DISBAND,
        JOIN,
        LEAVE,
        KICKED
    }
}
