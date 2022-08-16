package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class gPlayer {

    private Player player = null;
    private final UUID uuid;
    private Guild guild;
    private int guildId = 0;
    private GuildRank guildRank;
    private String name;
    private int power = Config.get().getInt("player max power");

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public int getGuildId() {
        return this.guildId;
    }

    public GuildRank getGuildRank() {
        return this.guildRank;
    }

    public String getName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
        if (guild != null) {
            this.guildId = guild.getId();
        } else {
            this.guildId = 0;
        }
    }

    public void setGuildRank(GuildRank rank) {
        this.guildRank = rank;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void changePower(int changeBy) {
        int playerMaxPower = Config.get().getInt("player max power");
        int playerMinPower = Config.get().getInt("player min power");
        this.power = Math.min(Math.max(this.power + changeBy, playerMinPower), playerMaxPower);
    }

    // For loading gPlayer from data
    public gPlayer(UUID uuid, int guildId, GuildRank guildRank, String name, int power) {
        this.uuid = uuid;
        this.guild = guild;
        this.guildId = guildId;
        this.guildRank = guildRank;
        this.name = name;
        this.power = power;
    }

    // For creating a gPlayer for the first time
    public gPlayer (Player player) {
        this.player = player;
        UUID uuid = player.getUniqueId();
        this.uuid = uuid;
        this.name = player.getName();
    }

    public void sendMessage(String msg) {
        getPlayer().sendMessage(msg);
    }

    public void sendSuccessMsg(String msg) {
        if (this.player != null) {
            pUtil.sendSuccessMsg(this.getPlayer(), msg);
        }
    }

    public void sendFailMsg(String msg) {
        if (this.player != null) {
            pUtil.sendFailMsg(this.getPlayer(), msg);
        }
    }

    public void sendNotifyMsg(String msg) {
        if (this.player != null) {
            pUtil.sendNotifyMsg(this.getPlayer(), msg);
        }
    }

    public Boolean isInGuild() { return getGuild() != null; }

    public void joinedNewGuild(Guild newGuild) {
        this.setGuild(newGuild);
        this.setGuildRank(newGuild.getRank(this));
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildRank(null);
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
            // Player is trying to overclaim land that is surrounded by the guild's claims
            else if (chunk.getGuild().isOverclaimable()) {
                this.sendFailMsg(Messages.getMsg("claiming.cannot overclaim because claim surrounded"));
            }
            else {
                this.sendFailMsg(Messages.getMsg("claiming.not overclaimable", chunk.getGuild()));
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

        // Claim chunk for Guild
        guild.claim(chunk);

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.claimed land", this));

        return true;
    }

    public boolean tryUnclaim(GuildChunk chunk) {

        // Is border
        if (chunk == null) {
            return false;
        }

        Guild guild = getGuild();

        // Check if chunk is owned by the player's guild
        if (chunk.getGuild() != guild) {
            this.sendFailMsg(Messages.getMsg("commands.unclaim.chunk not owned by guild"));
            return false;
        }

        // Unclaim chunk
        guild.unclaim(chunk);

        // Unclaim chunk on Board
        chunk.setWilderness();

        // Send Guild announcement
        guild.sendAnnouncement(Messages.getMsg("guild announcements.unclaimed land", this));

        return true;
    }
}
