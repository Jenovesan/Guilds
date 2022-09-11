package com.guildwars.guildwars.core.files;

import com.guildwars.guildwars.ConfigFile;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.MessageFile;

import java.io.File;

public class FileManager {

    public FileManager instance;

    public FileManager get() {
        return instance;
    }

    private final String folder;

    private final MessageFile messageFile;

    public MessageFile getMsgFile() {
        return messageFile;
    }

    private ConfigFile configFile;

    public ConfigFile getConfFile() {
        return configFile;
    }

    public FileManager(String folder) {
        // Initialization
        instance = this;
        this.folder = folder;

        // Make directory
        mkDir();

        // Load files
        messageFile = new MessageFile("core");
        configFile = new ConfigFile("core");
    }

    private void mkDir() {
        File dir = new File(GuildWars.getInstance().getDataFolder(), folder);

        // If the folder does not exist, create it
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
