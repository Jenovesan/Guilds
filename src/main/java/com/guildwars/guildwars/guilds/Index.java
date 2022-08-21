package com.guildwars.guildwars.guilds;

import java.util.HashMap;

public abstract class Index<T> {

    // -------------------------------------------- //
    // Name -> Obj
    // -------------------------------------------- //

    protected HashMap<String, T> name2Obj = new HashMap<>();

    public T getByName(String name) {
        return name2Obj.get(name);
    }

    // -------------------------------------------- //
    // Load
    // -------------------------------------------- //

    public abstract void load();
}
