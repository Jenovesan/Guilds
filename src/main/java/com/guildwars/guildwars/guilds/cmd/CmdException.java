package com.guildwars.guildwars.guilds.cmd;

public class CmdException extends Exception {

    private final String reason;

    public String getReason() {
        return reason;
    }

    public CmdException(String reason) {
        this.reason = reason;
    }
}
