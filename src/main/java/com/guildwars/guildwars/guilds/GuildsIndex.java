package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GuildsIndex extends Index<Guild> implements Listener {

    public static GuildsIndex i = new GuildsIndex();
    public static GuildsIndex get() {
        return i;
    }

    // -------------------------------------------- //
    // Id -> Guild
    // -------------------------------------------- //

    private HashMap<String, Guild> id2Guild = new HashMap<>();

    public Guild getById(String id) {
        return id2Guild.get(id);
    }


    // -------------------------------------------- //
    // Remove Guild on Creation & Disband
    // -------------------------------------------- //

    @EventHandler
    public void onGuildCreation(GuildCreationEvent event) {
        Guild guild = event.getGuild();

        id2Guild.put(guild.getId(), guild);
        name2Obj.put(guild.getName(), guild);
    }

    @EventHandler
    public void onGuildDisband(GuildDisbandEvent event) {
        Guild guild = event.getGuild();

        id2Guild.remove(guild.getId(), guild);
        name2Obj.remove(guild.getName(), guild);
    }

    // -------------------------------------------- //
    // On Load
    // -------------------------------------------- //

    public void load() {

        for (Guild guild : Guilds.get().getAll()) {
            // Id -> Guild
            id2Guild.put(guild.getId(), guild);
            // Name -> Guild
            name2Obj.put(guild.getName(), guild);
        }
    }
}
