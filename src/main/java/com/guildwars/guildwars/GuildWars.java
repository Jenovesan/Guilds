package com.guildwars.guildwars;

import com.guildwars.guildwars.guilds.cmd.GuildsCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GuildWars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadGuilds();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    public void loadGuilds() {
        //Load Guilds commands
        getCommand("guild").setExecutor(new GuildsCommandManager());

        //Load Guilds data
        com.guildwars.guildwars.guilds.files.messages.setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
