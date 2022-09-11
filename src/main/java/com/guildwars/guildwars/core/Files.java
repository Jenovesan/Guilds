package com.guildwars.guildwars.core;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;

import java.io.File;

public class Files {

    public Files instance;

    public Files get() {
        return instance;
    }

    private final String folder;

    private final Messages messageFile;

    public Messages getMessages() {
        return messageFile;
    }

    private Config configFile;

    public Config getConfig() {
        return configFile;
    }

    public Files(Plugin plugin) {
        // Initialization
        instance = this;
        this.folder = plugin.name().toLowerCase();

        // Make directory
        mkDir();

        // Load files
        messageFile = new Messages(plugin);
        configFile = new Config(plugin);
    }

    private void mkDir() {
        File dir = new File(GuildWars.getInstance().getDataFolder(), folder);

        // If the folder does not exist, create it
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
