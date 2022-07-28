package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.GuildsManager;
import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import com.guildwars.guildwars.guilds.files.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildWars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadGuilds();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        //Load Guilds data
        FileManager.setupFiles();

        //Load Guilds
        GuildsManager.loadGuilds();

        //Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
