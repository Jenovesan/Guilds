package com.guildwars.guildwars;

import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.engine.AutoClaim;
import com.guildwars.guildwars.guilds.engine.EngineIntegration;
import com.guildwars.guildwars.guilds.engine.Power;
import com.guildwars.guildwars.guilds.files.PlayerData;
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
        // Load Guilds data
        com.guildwars.guildwars.guilds.files.FileManager.setupFiles();

        // Load gPlayers
        gPlayers.get().load();

        // Load Guilds
        Guilds.get().load();

        // Fill gPlayers guilds
        gPlayers.get().loadGuilds();

        // Fill Guilds guilds
        Guilds.get().loadGuilds();

        // Load Guilds Index
        GuildsIndex.get().load();

        // Load gPlayers Index
        gPlayersIndex.get().load();

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
        unloadGuilds();
    }
}
