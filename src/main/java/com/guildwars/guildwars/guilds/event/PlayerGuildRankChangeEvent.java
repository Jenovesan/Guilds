package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.gPlayer;

public class PlayerGuildRankChangeEvent extends GuildsEvent {

    private final gPlayer player;
    private final GuildRank newRank;

    public gPlayer getPlayer() {
        return player;
    }

    public GuildRank getNewRank() {
        return newRank;
    }

    public PlayerGuildRankChangeEvent(gPlayer player, GuildRank newRank) {
        this.player = player;
        this.newRank = newRank;
    }
}
