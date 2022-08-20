package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.event.GuildRaidEndEvent;
import com.guildwars.guildwars.guilds.event.GuildRaidStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GuildsIndex implements Listener {

    // -------------------------------------------- //
    // Raids
    // -------------------------------------------- //

    private static HashMap<Integer, Integer> raids = new HashMap<>(); // Raidable, raiding

    public static HashMap<Integer, Integer> getRaids() {
        return raids;
    }

    @EventHandler
    public void onGuildRaidStart(GuildRaidStartEvent event) {
        getRaids().put(event.getRaidableGuild().getId(), event.getRaidingGuild().getId());
    }

    @EventHandler
    public void onGuildRaidEnd(GuildRaidEndEvent event) {
        getRaids().remove(event.getRaidableGuild().getId());
    }

    // -------------------------------------------- //
    // Remove Guild on Disband
    // -------------------------------------------- //

    @EventHandler
    public void onGuildDisband(GuildDisbandEvent event) {
        Guild guild = event.getGuild();

        getRaids().remove(guild);
    }
}
