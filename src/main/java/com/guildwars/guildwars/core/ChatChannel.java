package com.guildwars.guildwars.core;

import com.guildwars.guildwars.core.files.Config;

public enum ChatChannel {
    GUILD;

    public static String getRawChannelFormat(ChatChannel channel) {
        return Config.get().getString("chat channels." + channel.name());
    }
}
