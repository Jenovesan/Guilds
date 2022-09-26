package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.GPlayer;

public class PlayerLosePowerEvent extends GuildsEvent {

    private final GPlayer player;
    private final GPlayer killer;

    public GPlayer getKiller() {
        return killer;
    }

    public GPlayer getPlayer() {
        return player;
    }

    public PlayerLosePowerEvent(GPlayer player, GPlayer killer) {
        this.player = player;
        this.killer = killer;
    }
}
