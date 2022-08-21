package com.guildwars.guildwars.guilds;

import java.util.HashSet;
import java.util.Set;

public abstract class Coll<T> {

    private HashSet<T> coll = new HashSet<>();

    public abstract void load();

    public abstract void loadGuilds();

    public abstract void save(T obj);

    public Set<T> getAll() {
        return coll;
    }

    public void add(T obj) {
        coll.add(obj);
    }

    public void remove(T obj) {
        coll.remove(obj);
    }
}
