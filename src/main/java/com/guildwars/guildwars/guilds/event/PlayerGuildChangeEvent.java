package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGuildChangeEvent extends GuildsEvent {
    private final gPlayer player;
    private final Guild newGuild;
    private final Reason reason;

    public gPlayer getPlayer() {
        return player;
    }

    public Guild getNewGuild() {
        return newGuild;
    }

    public Reason getReason() {
        return reason;
    }

    public PlayerGuildChangeEvent(gPlayer player, Guild newGuild, Reason reason) {
        this.player = player;
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
