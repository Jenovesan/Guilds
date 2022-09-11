package com.guildwars.guildwars;

import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.core.files.FileManager;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.engine.EngineIntegration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildWars extends JavaPlugin {

    private static GuildWars instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        GuildWars.instance = this;

        loadCore();
        loadGuilds();
        activateEngines();
        registerListeners();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        System.out.println("-----Enabling Guilds-----");

        // Load Guilds data
        System.out.println("-setting up files");
        com.guildwars.guildwars.guilds.files.FileManager.setupFiles();

        // Load gPlayers
        System.out.println("-loading players");
        gPlayers.get().loadAll();

        // Load gPlayers Index
        System.out.println("-loading players index");
        gPlayersIndex.get().load();

        // Load Guilds
        System.out.println("-loading guilds");
        Guilds.get().loadAll();

        // Fill gPlayers guilds
        System.out.println("-loading player guilds");
        gPlayers.get().loadGuilds();

        // Load Guilds Index
        System.out.println("-loading guilds index");
        GuildsIndex.get().load();

        // Fill Guilds guilds
        System.out.println("-loading guilds' guilds");
        Guilds.get().loadGuilds();

        // Load Board
        System.out.println("-filling board");
        Board.fillBoard();

        // Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());
    }

    public void loadCore() {
        // Load file manager
        coreFM = new com.guildwars.guildwars.core.files.FileManager("core");
    }

    private static FileManager coreFM;

    public static FileManager getCoreFM() {
        return coreFM;
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
