package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.entity.Player;

public class gPlayer {

    private Player player = null;
    private final String uuid;
    private Guild guild;
    private GuildRank guildRank;
    private String name;
    private float power = Config.get().getInt("player max power");
    private boolean autoClaiming = false;
    private boolean autoMapping = false;

    public Player getPlayer() {
        return this.player;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public GuildRank getGuildRank() {
        return this.guildRank;
    }

    public String getName() {
        return this.name;
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

        this.changed();
    }

    public void setGuildRank(GuildRank rank) {
        this.guildRank = rank;

        this.changed();
    }

    public void setPower(int power) {
        this.power = power;

        this.changed();
    }

    public void setName(String name) {
        this.name = name;

        this.changed();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void changePower(float changeBy) {
        int playerMaxPower = Config.get().getInt("player max power");
        int playerMinPower = Config.get().getInt("player min power");
        this.power = Math.min(Math.max(this.power + changeBy, playerMinPower), playerMaxPower);
    }

    // For loading gPlayer from data
    public gPlayer(String uuid, GuildRank guildRank, String name, float power) {
        this.uuid = uuid;
        this.guildRank = guildRank;
        this.name = name;
        this.power = power;
    }

    // For creating a gPlayer for the first time
    public gPlayer (Player player) {
        this.player = player;
        this.uuid = String.valueOf(player.getUniqueId());
        this.name = player.getName();

        this.changed();
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
        this.setGuildRank(GuildRank.valueOf(Config.get().getString("join guild at rank")));

        this.changed();
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildRank(null);

        this.changed();
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

    public boolean isOnline() {
        return player != null;
    }

    public void setAutoClaiming(boolean autoClaiming) {
        this.autoClaiming = autoClaiming;
    }

    public void setAutoMapping(boolean autoMapping) {
        this.autoMapping = autoMapping;
    }

    private void changed() {
        PlayerData.get().save(this);
    }
}
