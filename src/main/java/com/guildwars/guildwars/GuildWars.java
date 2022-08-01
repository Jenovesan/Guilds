package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.GuildsFastData;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.files.FileManager;
import com.guildwars.guildwars.guilds.gPlayers;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildWars extends JavaPlugin {

    private static GuildWars instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        GuildWars.instance = this;

        loadGuilds();
        registerListeners();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        //Load Guilds data
        FileManager.setupFiles();

        //Load Guilds
        Guilds.loadGuilds();

        //Load fast guild data
        GuildsFastData.loadPlayersGuildsIds();

        //Load gPlayers
        gPlayers.loadGPlayers();

        //Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());
    }

    public void registerListeners() {
        //Guilds
        getServer().getPluginManager().registerEvents(new GuildsFastData(), this);
        getServer().getPluginManager().registerEvents(new gPlayers(), this);
    }

    public static GuildWars getInstance(){
        return GuildWars.instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
