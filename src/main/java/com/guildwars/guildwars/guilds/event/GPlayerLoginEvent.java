package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.entity.Player;

public class GPlayerLoginEvent extends GuildsEvent {
    private final Player player;
    private final com.guildwars.guildwars.guilds.gPlayer gPlayer;

    public Player getPlayer() {
        return player;
    }

    public gPlayer getGPlayer() {
        return gPlayer;
    }

    public GPlayerLoginEvent(Player player, gPlayer gPlayer) {
        this.player = player;
        this.gPlayer = gPlayer;
    }
}
