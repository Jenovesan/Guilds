package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.core.CorePlayer;
import com.guildwars.guildwars.guilds.event.GuildUnclaimEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;
import org.bukkit.entity.Player;

public class gPlayer extends CorePlayer {

    private Guild guild;
    private GuildRank guildRank;
    private float power = Config.get().getInt("player max power");
    private boolean autoClaiming = false;
    private boolean autoMapping = false;

    public Guild getGuild() {
        return this.guild;
    }

    public GuildRank getGuildRank() {
        return this.guildRank;
    }

    public float getPower() {
        return this.power;
    }

    public boolean isAutoClaiming() {
        return this.autoClaiming;
    }

    public boolean isAutoMapping() {
        return this.autoMapping;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setGuildRank(GuildRank rank) {
        this.guildRank = rank;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void changePower(float changeBy) {
        int playerMaxPower = Config.get().getInt("player max power");
        int playerMinPower = Config.get().getInt("player min power");
        this.power = Math.min(Math.max(this.power + changeBy, playerMinPower), playerMaxPower);
    }

    // For loading gPlayer from data
    public gPlayer(String uuid, GuildRank guildRank, String name, float power) {
        super(uuid, name);
        this.guildRank = guildRank;
        this.power = power;
    }

    // For creating a gPlayer for the first time
    public gPlayer (Player player) {
        super(player);
    }

    public Boolean isInGuild() { return getGuild() != null; }

    public void joinedNewGuild(Guild newGuild) {
        this.setGuild(newGuild);
        this.setGuildRank(GuildRank.valueOf(Config.get().getString("join guild at rank")));

        PlayerData.get().save(this);
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildRank(null);

        PlayerData.get().save(this);
    }

    public boolean tryClaim(GuildChunk chunk) {

        // Checks
        Guild guild = getGuild();
        // Check if guild can claim
        if (!guild.canClaim()) {
            this.sendFailMsg(Messages.getMsg("claiming.not enough power"));
            return false;
        }

        // Is border
        if (chunk == null) {
            return false;
        }

        // Check if it's claimable
        if (!chunk.isClaimable()) {
            // Player is trying to claim their own chunk
            if (chunk.getGuild() == guild) {
                this.sendFailMsg(Messages.getMsg("claiming.claiming own land"));
            }
            return false;
        }

        // If it is not the guild's first claim, check if it has a connecting claim
        if (guild.getNumberOfClaims() > 0 && !chunk.hasConnectingClaim(guild)) {
            this.sendFailMsg(Messages.getMsg("claiming.no connecting claim"));
            return false;
        }

        // Claim chunk
        chunk.claim(guild);

        // Claim chunk
        guild.claim(chunk);

        // Save data
        GuildData.get().save(guild);

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.claimed single land", this));

        return true;
    }

    public int tryClaim(GuildChunk[] chunks) {
        int successfulClaims = 0;

        for (GuildChunk chunk : chunks) {
            if (!guild.canClaim()) {
                this.sendFailMsg(Messages.getMsg("claiming.ran out of power"));
                break;
            }

            // Is outlands
            if (chunk == null) continue;

            // Chunk already has host guild
            if (!chunk.isClaimable()) continue;

            // Claim chunk
            chunk.claim(guild);

            // Claim chunk
            guild.claim(chunk);

            successfulClaims++;
        }

        // Save data
        GuildData.get().save(guild);

        return successfulClaims;
    }

    public boolean tryUnclaim(GuildChunk chunk) {

        // Is border
        if (chunk == null) {
            return false;
        }

        // Check if chunk is owned by the player's guild
        if (chunk.getGuild() != guild) {
            this.sendFailMsg(Messages.getMsg("commands.unclaim.chunk not owned by guild"));
            return false;
        }

        // Unclaim chunk
        guild.unclaim(chunk);

        // Save data
        GuildData.get().save(guild);

        // Unclaim chunk on Board
        chunk.setWilderness();

        // Call event
        GuildUnclaimEvent guildUnclaimEvent = new GuildUnclaimEvent(guild, chunk);
        guildUnclaimEvent.run();

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.unclaimed single land", this));

        return true;
    }

    public int tryUnclaim(GuildChunk[] chunks) {
        int successfulUnclaims = 0;
        Guild guild = getGuild();

        for (GuildChunk chunk : chunks) {
            // Is outlands
            if (chunk == null) continue;
            // Not owned by player's guild
            if (chunk.getGuild() != guild) continue;

            // Unclaim chunk
            guild.unclaim(chunk);

            // Unclaim chunk
            chunk.setWilderness();

            // Call event
            GuildUnclaimEvent guildUnclaimEvent = new GuildUnclaimEvent(guild, chunk);
            guildUnclaimEvent.run();

            successfulUnclaims++;
        }

        // Save data
        GuildData.get().save(guild);

        return successfulUnclaims;
    }

    public void setAutoClaiming(boolean autoClaiming) {
        this.autoClaiming = autoClaiming;
    }

    public void setAutoMapping(boolean autoMapping) {
        this.autoMapping = autoMapping;
    }
}
