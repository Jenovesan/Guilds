package com.guildwars.guildwars;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GuildWarsFile<T> {

    private final String name;
    private final String folder;
    private FileConfiguration configuration;
    protected HashMap<String, T> messagesCache = new HashMap<>();

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public GuildWarsFile(String name, Plugin plugin) {
        this.name = name;
        this.folder = plugin.name().toLowerCase();

        loadFile();
    }

    protected void loadFile() {
        try {
            File out = new File(GuildWars.getInstance().getDataFolder(), folder + "/" + name + ".yml");
            if (!out.exists()) {
                GuildWars.getInstance().getDataFolder().mkdir();
                out.createNewFile();
            }

            FileConfiguration file = YamlConfiguration.loadConfiguration(out);
            InputStream in = GuildWars.getInstance().getResource(folder + "/" + name + ".yml");
            if (in != null) {
                InputStreamReader inReader = new InputStreamReader(in);
                file.setDefaults(YamlConfiguration.loadConfiguration(inReader));
                file.options().copyDefaults(true);
                file.save(out);
            }

            this.configuration = file;

        } catch (IOException ex) {
            System.err.println("Failed to load " + folder + "/" + name + ".yml file");
            ex.printStackTrace();
        }
    }
}
