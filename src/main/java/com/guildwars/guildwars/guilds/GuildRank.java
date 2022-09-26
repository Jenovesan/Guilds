package com.guildwars.guildwars.guilds;

import java.util.HashMap;

public enum GuildRank {
    RECRUIT(1),
    MEMBER(2),
    MOD(3),
    COLEADER(4),
    LEADER(5);

    private final int level;

    public int getLevel() {
        return level;
    }

    GuildRank (int level) {
        this.level = level;
    }

    public static int higherByAmount(GuildRank rank1, GuildRank rank2) {
        return rank1.getLevel() - rank2.getLevel();
    }

    // Used to get GuildRanks by level
    private final static HashMap<Integer, GuildRank> levelsWithGuildRanks = new HashMap<>();
    static {
        for (GuildRank rank : GuildRank.values()) {
            levelsWithGuildRanks.put(rank.getLevel(), rank);
        }
    }

    public static GuildRank getGuildRankByLevel(int level) {
        return levelsWithGuildRanks.get(level);
    }

    public static GuildRank[] getAll() {
        return GuildRank.values();
    }
}
