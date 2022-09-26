package com.guildwars.guildwars.guilds.entity;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.core.CorePlayer;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GuildUnclaimEvent;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.utils.rUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GPlayer extends CorePlayer implements RelationParticipator {

    private Guild guild;
    private GuildRank guildRank;
    private final int maxPower = Config.get(Plugin.GUILDS).getInt("player max power");
    private float power = maxPower;
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

    public int getFloorPower() {
        return (int) Math.floor(power);
    }

    public boolean isAutoClaiming() {
        return this.autoClaiming;
    }

    public boolean isAutoMapping() {
        return this.autoMapping;
    }

    private void setGuild(Guild guild) {
        this.guild = guild;
    }

    private void setGuildRank(GuildRank rank, boolean applyChanged) {
        this.guildRank = rank;
        if (applyChanged) changed();
    }

    public void setGuildRank(GuildRank rank) {
        setGuildRank(rank, true);
    }

    public void createdGuild(Guild guild) {
        this.guild = guild;
        this.guildRank = GuildRank.LEADER;
        changed();
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void changePower(float changeBy) {
        int playerMaxPower = Config.get(Plugin.GUILDS).getInt("player max power");
        int playerMinPower = Config.get(Plugin.GUILDS).getInt("player min power");
        this.power = Math.min(Math.max(this.power + changeBy, playerMinPower), playerMaxPower);

        changed();
    }

    // For loading gPlayer from data
    public GPlayer(UUID uuid, Guild guild, GuildRank guildRank, String name, float power) {
        super(uuid, name);
        this.guild = guild;
        this.guildRank = guildRank;
        this.power = power;
    }

    // For creating a gPlayer for the first time
    public GPlayer(Player player) {
        super(player);
    }

    public Boolean isInGuild() { return getGuild() != null; }

    public void joinedNewGuild(Guild newGuild) {
        this.setGuild(newGuild);
        this.setGuildRank(GuildRank.valueOf(Config.get(Plugin.GUILDS).getString("join guild at rank")), false);

        changed();
    }

    public void leftGuild() {
        this.setGuild(null);
        this.setGuildRank(null, false);

        changed();
    }

    public boolean tryClaim(GuildChunk chunk) {
        return tryClaim(new GuildChunk[]{chunk}, true, true) == 1;
    }

    public int tryClaim(GuildChunk[] chunks, boolean claiming, boolean single) {
        int successes = 0;

        for (GuildChunk chunk : chunks) {
            // Players cannot claim chunks in outlands
            if (chunk == null) {
                if (single) sendFailMsg(Messages.get(Plugin.GUILDS).get("claiming.outlands"));
                continue;
            }

            // Player is trying to claim
            if (claiming) {

                // Checks

                // Chunk might not be claimable
                if (!chunk.isClaimable()) {
                    if (single) {
                        // Player is trying to claim land that their guild already owns
                        if (chunk.getGuild() == guild) sendFailMsg(Messages.get(Plugin.GUILDS).get("claiming.claiming own land"));
                            // Player is trying to claim someone else's land
                        else sendFailMsg(Messages.get(Plugin.GUILDS).get("claiming.other guild ownership", guild.describe(chunk.getGuild())));
                    }
                    continue;
                }

                // Player cannot claim if their guild does not have any extra power
                if (!guild.canClaim()) {
                    sendFailMsg(Messages.get(Plugin.GUILDS).get("claiming.not enough power"));
                    break;
                }

                // If it is not the guild's first claim, it has to have a connecting claim
                if (guild.hasClaim() && !chunk.hasConnectingClaim(guild)) {
                    if (single) sendFailMsg(Messages.get(Plugin.GUILDS).get("claiming.no connecting claim"));
                    continue;
                }

                // Apply

                chunk.claim(guild);
            }
            // Player is trying to unclaim
            // Player cannot unclaim territory unless their guild owns it
            else {

                // Checks

                if (chunk.getGuild() != guild) {
                    if (single) sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.unclaim.chunk not owned by guild"));
                    continue;
                }

                // Apply

                chunk.setWilderness();

                // Call Event
                GuildUnclaimEvent guildUnclaimEvent = new GuildUnclaimEvent(guild, chunk);
                guildUnclaimEvent.run();
            }

            successes++;
        }

        return successes;
    }

    public void setAutoClaiming(boolean autoClaiming) {
        this.autoClaiming = autoClaiming;
    }

    public void setAutoMapping(boolean autoMapping) {
        this.autoMapping = autoMapping;
    }

    @Override
    public Relation getRelationTo(RelationParticipator participator) {
        return rUtil.getRelation(this, participator);
    }

    @Override
    public String describe(RelationParticipator participator) {
        return rUtil.describe(this, participator);
    }

    private void changed() {
        PlayerData.get().save(this);
    }
}
