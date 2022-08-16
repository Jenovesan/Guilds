package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Messages;

public class GuildChunk {

    private Guild guild;
    private int guildId;
    private final int[] boardLocation;

    public void claim(Guild hostGuild) {
        if (this.guild != null) {
            getGuild().removeClaim(this);
            getGuild().sendAnnouncement(Messages.getMsg("guild announcements.overclaimed", hostGuild));
        }
        this.guild = hostGuild;
        this.guildId = hostGuild.getId();
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
        return this.isWilderness();
    }

    public boolean hasConnectingClaim(Guild guild) {
        int x = boardLocation[0];
        int z = boardLocation[1];
        if (Board.getBoard()[x][z + 1].getGuild() != null && Board.getBoard()[x][z + 1].getGuild() == guild) {
            return true;
        }
        if (Board.getBoard()[x][z - 1].getGuild()  != null && Board.getBoard()[x][z - 1].getGuild() == guild) {
            return true;
        }
        if (Board.getBoard()[x + 1][z].getGuild() != null && Board.getBoard()[x + 1][z].getGuild() == guild) {
            return true;
        }
        if (Board.getBoard()[x - 1][z].getGuild() != null && Board.getBoard()[x - 1][z].getGuild() == guild) {
            return true;
        }
        return false;
    }

    public boolean isWilderness() {
        return this.getGuildId() == 0;
    }

    public void setWilderness() {
        this.guild = null;
        this.guildId = 0;
    }

}
