package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.engine.Raiding;
import com.guildwars.guildwars.guilds.event.*;
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
    // Id -> Guild
    // -------------------------------------------- //

    private static HashMap<Integer, Guild> id2Guild = new HashMap<>(); // Raidable, raiding

    private static HashMap<Integer, Guild> getId2Guild() {
        return id2Guild;
    }

    public static Guild getGuildById(int id) {
        return getId2Guild().get(id);
    }

    // -------------------------------------------- //
    // Name -> Guild
    // -------------------------------------------- //

    private static HashMap<String, Guild> name2Guild = new HashMap<>(); // Raidable, raiding

    private static HashMap<String, Guild> getName2Guild() {
        return name2Guild;
    }

    public static Guild getGuildByName(String name) {
        return getName2Guild().get(name);
    }

    @EventHandler
    public void updateNameOnGuildNameChange(GuildNameChangeEvent event) {
        name2Guild.remove(event.getOldName());
        name2Guild.put(event.getNewName(), event.getGuild());
    }

    // -------------------------------------------- //
    // Remove Guild on Creation & Disband
    // -------------------------------------------- //

    @EventHandler
    public void onGuildCreation(GuildCreationEvent event) {
        Guild guild = event.getGuild();

        getId2Guild().put(guild.getId(), guild);
        getName2Guild().put(guild.getName(), guild);
    }

    @EventHandler
    public void onGuildDisband(GuildDisbandEvent event) {
        Guild guild = event.getGuild();

        getRaids().remove(guild.getId());
        getId2Guild().remove(guild.getId());
        getName2Guild().remove(guild.getName());
    }

    // -------------------------------------------- //
    // On Load
    // -------------------------------------------- //

    public static void load() {
        // Raids
        for (Guild raidingGuild : Raiding.getRaidingGuilds()) {
            raids.put(raidingGuild.getRaid(), raidingGuild.getId());
        }

        for (Guild guild : Guilds.getAllGuilds()) {
            // Id -> Guild
            getId2Guild().put(guild.getId(), guild);
            // Name -> Guild
            getName2Guild().put(guild.getName(), guild);
        }
    }
}
