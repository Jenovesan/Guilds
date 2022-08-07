package com.guildwars.guildwars.guilds;

public class GuildChunk {

    private Guild guild;
    private int guildId;

    public void setNewGuild(Guild hostGuild) {
        this.guild = hostGuild;
        this.guildId = hostGuild.getId();
    }

    public Guild getGuild() {
        return guild;
    }

    public int getGuildId() {
        return guildId;
    }

    public GuildChunk(Guild hostGuild) {
        this.guild = hostGuild;
        if (hostGuild != null) {
            this.guildId = hostGuild.getId();
        }
    }

}
