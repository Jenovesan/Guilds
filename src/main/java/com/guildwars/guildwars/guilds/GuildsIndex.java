package com.guildwars.guildwars.guilds;

import java.util.HashMap;

public class GuildsIndex extends Index<Guild> {

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
    // Update name on guild name change
    // -------------------------------------------- //

    public void updateGuildName(Guild guild, String oldName, String newName) {
        name2Obj.remove(formatName(oldName));
        name2Obj.put(formatName(newName), guild);
    }


    // -------------------------------------------- //
    // Remove Guild on Creation & Disband
    // -------------------------------------------- //

    @Override
    public void add(Guild guild) {
        id2Guild.put(guild.getId(), guild);
        name2Obj.put(formatName(guild.getName()), guild);
    }

    public void remove(Guild guild) {
        id2Guild.remove(guild.getId(), guild);
        name2Obj.remove(formatName(guild.getName()), guild);
    }

    @Override
    public void updateName(Guild guild, String newName, String oldName) {
        name2Obj.remove(formatName(oldName));
        name2Obj.put(formatName(newName), guild);
    }

    // -------------------------------------------- //
    // On Load
    // -------------------------------------------- //

    public void load() {
        for (Guild guild : Guilds.get().getAll()) {
            // Id -> Guild
            id2Guild.put(guild.getId(), guild);
            // Name -> Guild
            name2Obj.put(formatName(guild.getName()), guild);
        }
    }

    private String formatName(String name) {
        return name.toLowerCase();
    }
}
