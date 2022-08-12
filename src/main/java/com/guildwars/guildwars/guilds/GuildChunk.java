package com.guildwars.guildwars.guilds;

public class GuildChunk {

    private Guild guild;
    private int guildId;
    private final int[] boardLocation;

    public void claim(Guild hostGuild) {
        this.guild = hostGuild;
        if (hostGuild != null) {
            this.guildId = hostGuild.getId();
        }
    }

    public Guild getGuild() {
        return guild;
    }

    public int getGuildId() {
        return guildId;
    }

    public int[] getBoardLocation() {
        return this.boardLocation;
    }

    public GuildChunk(Guild hostGuild, int[] boardLocation) {
        this.guild = hostGuild;
        if (hostGuild != null) {
            this.guildId = hostGuild.getId();
        }
        this.boardLocation = boardLocation;
    }

    public boolean isClaimable() {
        // Chunk is claimable if no guild owns it, or the guild that owns it has less power than claims (overclaimable)
        return this.getGuildId() == 0 || this.getGuild().getNumberOfClaims() > this.getGuild().getPower();
    }

    public boolean isWilderness() {
        return this.getGuildId() == 0;
    }

    public void setWilderness() {
        this.guild = null;
        this.guildId = 0;
    }

}
