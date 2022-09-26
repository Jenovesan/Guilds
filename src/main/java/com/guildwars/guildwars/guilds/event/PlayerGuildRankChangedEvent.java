package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.entity.GPlayer;

public class PlayerGuildRankChangedEvent extends GuildsEvent {

    private final GPlayer player;
    private final GuildRank newRank;

    public GPlayer getPlayer() {
        return player;
    }

    public GuildRank getNewRank() {
        return newRank;
    }

    public PlayerGuildRankChangedEvent(GPlayer player, GuildRank newRank) {
        this.player = player;
        this.newRank = newRank;
    }
}
