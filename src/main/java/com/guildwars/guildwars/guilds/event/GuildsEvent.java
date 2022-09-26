package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.GuildWars;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildsEvent extends Event {

    public void run() {
        Bukkit.getPluginManager().callEvent(this);
    }

    public void runSync() {Bukkit.getScheduler().runTask(GuildWars.getInstance(), this::run);}

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
