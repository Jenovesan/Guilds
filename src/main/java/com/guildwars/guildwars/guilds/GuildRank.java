package com.guildwars.guildwars.guilds;

public enum GuildRank {
    NONE(0),
    RECRUIT(1),
    MEMBER(2),
    MOD(3),
    COLEADER(4),
    LEADER(5);

    public final int level;
    GuildRank (int level) {
        this.level = level;
    }
}
