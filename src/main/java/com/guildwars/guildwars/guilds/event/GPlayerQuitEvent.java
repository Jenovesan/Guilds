package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;

public class GPlayerQuitEvent extends GuildsEvent {
    private final gPlayer gPlayer;

    public gPlayer getGPlayer() {
        return gPlayer;
    }

    public GPlayerQuitEvent(gPlayer gPlayer) {
        this.gPlayer = gPlayer;
    }
}
