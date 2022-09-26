package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.GPlayer;
import org.bukkit.entity.Player;

public class GPlayerQuitEvent extends GuildsEvent {
    private final GPlayer gPlayer;
    private final Player player;

    public GPlayer getGPlayer() {
        return gPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public GPlayerQuitEvent(GPlayer gPlayer, Player player) {
        this.gPlayer = gPlayer;
        this.player = player;
    }
}
