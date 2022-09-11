package com.guildwars.guildwars.core;

import com.guildwars.guildwars.GuildWars;

public enum ChatChannel {
    GUILD;

    public static String getRawChannelFormat(ChatChannel channel) {
        return GuildWars.getCoreFM().getConfFile().getString("chat channels." + channel.name());
    }
}
