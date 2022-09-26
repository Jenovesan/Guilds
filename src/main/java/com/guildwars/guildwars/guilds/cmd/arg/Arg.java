package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.cmd.CmdException;

public abstract class Arg<T> {

    private final boolean varargs;

    public boolean isVarargs() {
        return varargs;
    }

    private final boolean required;

    public boolean isRequired() {
        return required;
    }

    private final String asString;

    public String asString() {
        return asString;
    }

    public Arg(boolean required, String asString) {
        this.required = required;
        this.asString = asString;
        this.varargs = false;
    }

    public Arg(boolean required, String asString, boolean varargs) {
        this.required = required;
        this.asString = asString;
        this.varargs = varargs;
    }

    public abstract T read(String arg, GPlayer gPlayer) throws CmdException;
}
