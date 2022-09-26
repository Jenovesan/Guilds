package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.GPlayer;
import org.bukkit.entity.Player;

public class GPlayerLoginEvent extends GuildsEvent {
    private final Player player;
    private final GPlayer gPlayer;

    public Player getPlayer() {
        return player;
    }

    public GPlayer getGPlayer() {
        return gPlayer;
    }

    public GPlayerLoginEvent(Player player, GPlayer gPlayer) {
        this.player = player;
        this.gPlayer = gPlayer;
    }
}
