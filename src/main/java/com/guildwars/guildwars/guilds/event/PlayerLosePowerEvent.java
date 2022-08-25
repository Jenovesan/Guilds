package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;

public class PlayerLosePowerEvent extends GuildsEvent {

    private final gPlayer player;
    private final gPlayer killer;

    public gPlayer getKiller() {
        return killer;
    }

    public gPlayer getPlayer() {
        return player;
    }

    public PlayerLosePowerEvent(gPlayer player, gPlayer killer) {
        this.player = player;
        this.killer = killer;
    }
}
