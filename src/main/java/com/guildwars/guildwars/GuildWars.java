package com.guildwars.guildwars;

import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.core.Files;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.gCommandBase;
import com.guildwars.guildwars.guilds.engine.EngineIntegration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildWars extends JavaPlugin {

    private static GuildWars instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        GuildWars.instance = this;

        System.out.println("Loading...");
        loadCore();
        loadGuilds();
        System.out.println("Enabling...");
        activateEngines();
        registerListeners();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        System.out.println("-----Enabling Guilds-----");

        // Load Guilds data
        System.out.println("-setting up files");
        new Files(Plugin.GUILDS);

        // Load Guilds
        System.out.println("-loading guilds");
        Guilds.get().load();

        // Load gPlayers
        System.out.println("-loading players");
        GPlayers.get().load();

        // Load Board
        System.out.println("-loading board");
        Board.get().load();

        // Load Index
        System.out.println("-loading indexes");
        Indexing.get().load();

        // Load Guilds commands
        getCommand("guild").setExecutor(gCommandBase.get());
    }

    public void loadCore() {
        // Load file manager
        new Files(Plugin.CORE);
    }


    public void activateEngines() {
        // Guilds
        EngineIntegration.activateEngines();
    }

    public void registerListeners() {
        // Core
        getServer().getPluginManager().registerEvents(new ChatChannels(), this);
    }

    public static GuildWars getInstance(){
        return GuildWars.instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
