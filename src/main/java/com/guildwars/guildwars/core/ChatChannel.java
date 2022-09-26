package com.guildwars.guildwars.core;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;

public enum ChatChannel {
    GUILD,
    PUBLIC
    ;

    public static String getRawChannelFormat(ChatChannel channel) {
        return Config.get(Plugin.CORE).getString("chat channels." + channel.name());
    }
}
