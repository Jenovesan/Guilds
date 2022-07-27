package com.guildwars.guildwars;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildWars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guild Wars Successfully Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
