package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import org.bukkit.ChatColor;

public enum Relation {

    OWN(2,false),
    ENEMY(1,true, "e"),
    NEUTRAL(0, true, "truce", "t", "n");

    private final int value;
    private final String[] aliases;
    private final boolean requestable;

    public ChatColor getPrimaryColor() {
        return ChatColor.valueOf(Config.get(Plugin.GUILDS).getString("relations.primary." + name()));
    }

    public ChatColor getSecondaryColor() {
        return ChatColor.valueOf(Config.get(Plugin.GUILDS).getString("relations.secondary." + name()));
    }

    public int getValue() {
        return value;
    }

    public boolean isRequestable() {
        return requestable;
    }

    public String[] getAliases() {
        return aliases;
    }

    Relation(int value, boolean settable, String... aliases) {
        this.requestable = settable;
        this.value = value;
        this.aliases = aliases;
    }

    public String describe() {
        return Messages.get(Plugin.GUILDS).get("relations." + this.name());
    }
}
