package com.guildwars.guildwars;

import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.engine.AutoClaim;
import com.guildwars.guildwars.guilds.engine.PlayerChunkUpdate;
import com.guildwars.guildwars.guilds.engine.Power;
import com.guildwars.guildwars.guilds.engine.Raiding;
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
        registerListeners();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        // Load Guilds data
        com.guildwars.guildwars.guilds.files.FileManager.setupFiles();

        // Load gPlayers
        gPlayers.getgInstance().load();

        // Load Guilds
        Guilds.get().load();

        // Fill gPlayers guilds
        gPlayers.getgInstance().loadGuilds();

        // Fill Guilds guilds
        Guilds.get().loadGuilds();

        // Load Guilds Index
        GuildsIndex.get().load();

        // Load gPlayers Index
        gPlayersIndex.get().load();

        // Load Board
        Board.fillBoard();

        // Load power
        Power.load();
        Raiding.get().load();

        // Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());

        // Start Runnables
        Power.run();
        Raiding.run();
        PlayerChunkUpdate.run();
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
        getServer().getPluginManager().registerEvents(new gPlayers(), this);
        getServer().getPluginManager().registerEvents(new Guilds(), this);
        getServer().getPluginManager().registerEvents(new Power(), this);
        getServer().getPluginManager().registerEvents(new AutoClaim(), this);
        getServer().getPluginManager().registerEvents(new Raiding(), this);
        getServer().getPluginManager().registerEvents(new GuildsIndex(), this);
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
