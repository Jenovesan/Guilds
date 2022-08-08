package com.guildwars.guildwars;

import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.Board;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.GuildsFastData;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.engine.Power;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.gPlayers;
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
        registerListeners();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        // Load Guilds data
        com.guildwars.guildwars.guilds.files.FileManager.setupFiles();

        // Load Guilds
        Guilds.loadGuilds();

        // Load fast guild data
        GuildsFastData.loadPlayersGuildsIds();

        // Load gPlayers
        gPlayers.loadGPlayers();

        // Load Board
        Board.fillBoard();

        // Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());
    }

    public void unloadGuilds() {
        // Save player data
        PlayerData.saveAllPlayerData();
    }

    public void loadCore() {
        com.guildwars.guildwars.core.files.FileManager.setupFiles();
    }

    public void registerListeners() {
        // Core
        getServer().getPluginManager().registerEvents(new ChatChannels(), this);

        // Guilds
        getServer().getPluginManager().registerEvents(new GuildsFastData(), this);
        getServer().getPluginManager().registerEvents(new gPlayers(), this);
        getServer().getPluginManager().registerEvents(new Guilds(), this);
        getServer().getPluginManager().registerEvents(new Power(), this);
    }

    public static GuildWars getInstance(){
        return GuildWars.instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unloadGuilds();
    }
}
