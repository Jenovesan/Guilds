package com.guildwars.guildwars.guilds;

import java.util.HashMap;

public enum GuildRank {
    RECRUIT(1),
    MEMBER(2),
    MOD(3),
    COLEADER(4),
    LEADER(5);

    public final int level;
    GuildRank (int level) {
        this.level = level;
    }

    public static int higherByAmount(GuildRank rank1, GuildRank rank2) {
        return rank1.level - rank2.level;
    }

    // Used to get GuildRanks by level
    private final static HashMap<Integer, GuildRank> levelsWithGuildRanks = new HashMap<>();
    static {
        for (GuildRank rank : GuildRank.values()) {
            levelsWithGuildRanks.put(rank.level, rank);
        }
    }

    public static GuildRank getGuildRankByLevel(int level) {
        return levelsWithGuildRanks.get(level);
    }

    public static GuildRank[] getAll() {
        return GuildRank.values();
    }
}
