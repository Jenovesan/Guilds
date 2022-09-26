package com.guildwars.guildwars.guilds.entity;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.utils.gUtil;
import com.guildwars.guildwars.guilds.utils.rUtil;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class Guild implements RelationParticipator {

    private final String id;
    private String name;
    private String description;
    private HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
    private HashSet<Invitation> invites = new HashSet<>();
    private HashMap<Guild, Relation> relationWishes = new HashMap<>();
    private Guild raidedBy;
    private long raidEndTime;
    private Location home;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashSet<GPlayer> getPlayers() {
        return Indexing.get().getGuildPlayers(this);
    }

    public HashMap<GuildPermission, GuildRank> getPermissions() {
        return permissions;
    }

    public HashSet<Invitation> getInvites() {
        return invites;
    }

    public Guild getRaidedBy() {
        return raidedBy;
    }

    public long getRaidEndTime() {
        return raidEndTime;
    }

    public Location getHome() {
        return home;
    }

    public HashMap<Guild, Relation> getRelationWishes() {
        return relationWishes;
    }

    // Creating a new guild
    public Guild(String name, String description) {
        this.id = gUtil.getNewUUID();
        this.name = name;
        this.description = description;
        loadDefaults();

        // Add guild to Guilds
        Guilds.get().add(this);

        changed();
    }

    // Loading an existing guild on startup
    public Guild(String id,
                 String name,
                 String description,
                 HashMap<GuildPermission, GuildRank> permissions,
                 HashMap<Guild, Relation> relationWishes,
                 Guild raidedBy,
                 long raidEndTime,
                 Location home) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.relationWishes = relationWishes;
        this.raidedBy = raidedBy;
        this.raidEndTime = raidEndTime;
        this.home = home;
    }

    public void loadDefaults() {
        // Permissions
        this.permissions.put(GuildPermission.INVITE, Config.get(Plugin.GUILDS).getGuildRank("default permissions.invite"));
        this.permissions.put(GuildPermission.SET_DESC, Config.get(Plugin.GUILDS).getGuildRank("default permissions.set_desc"));
        this.permissions.put(GuildPermission.SET_NAME, Config.get(Plugin.GUILDS).getGuildRank("default permissions.set_name"));
        this.permissions.put(GuildPermission.CHAT, Config.get(Plugin.GUILDS).getGuildRank("default permissions.chat"));
        this.permissions.put(GuildPermission.RELATIONS, Config.get(Plugin.GUILDS).getGuildRank("default permissions.relations"));
        this.permissions.put(GuildPermission.CLAIM, Config.get(Plugin.GUILDS).getGuildRank("default permissions.claim"));
        this.permissions.put(GuildPermission.UNCLAIM, Config.get(Plugin.GUILDS).getGuildRank("default permissions.unclaim"));
        this.permissions.put(GuildPermission.UNCLAIM_ALL, Config.get(Plugin.GUILDS).getGuildRank("default permissions.unclaim_all"));
        this.permissions.put(GuildPermission.SETHOME, Config.get(Plugin.GUILDS).getGuildRank("default permissions.sethome"));
        this.permissions.put(GuildPermission.HOME, Config.get(Plugin.GUILDS).getGuildRank("default permissions.home"));
        this.permissions.put(GuildPermission.PROMOTE, Config.get(Plugin.GUILDS).getGuildRank("default permissions.promote"));
    }

    public void setName(String newName) {
        this.name = newName;
        changed();
    }

    public void setDescription(String desc) {
        this.description = desc;
        changed();
    }

    public void addInvite(Invitation invite) {
        invites.add(invite);
    }

    public void removeInvite(Invitation invite) {
        invites.remove(invite);
    }

    public List<GPlayer> getOnlinePlayers() {
        return getPlayers().stream().filter(GPlayer::isOnline).collect(Collectors.toList());
    }

    public int getNumberOfOnlinePlayers() {
        return getOnlinePlayers().size();
    }

    public boolean isInvited(GPlayer gPlayer) {
        UUID uuid = gPlayer.getUUID();
        for (Invitation invite : invites) {
            if (invite.getPlayerUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
     }

     public int getPlayerCount() {
        return getPlayers().size();
     }

    public boolean isFull() {
        int maxPlayersInGuild = Config.get(Plugin.GUILDS).getInt("max players in guild");
        return getPlayerCount() >= maxPlayersInGuild;
    }

    public void sendAnnouncement(String msg, GPlayer... exclude) {
        // Get online players in the guild
        List<GPlayer> onlinePlayers = getOnlinePlayers();

        // Remove excluded players
        Arrays.stream(exclude).forEach(onlinePlayers::remove);

        // Send announcement to each player
        onlinePlayers.forEach(player -> player.sendNotifyMsg(msg));
    }

    public int getPower() {
        return getPlayers().stream().mapToInt(GPlayer::getFloorPower).sum();
    }

    public int getMaxPower() {
        return getPlayers().size() * Config.get(Plugin.GUILDS).getInt("player max power");
    }

    public void setRaidedBy(Guild guild) {
        this.raidedBy = guild;
        changed();
    }

    public void setRaidEndTime(long time) {
        this.raidEndTime = time;
    }

    public boolean isGettingRaided() {
        return this.getRaidedBy() != null;
    }

    public void sendBroadcast(String title, String subtitle) {
        for (GPlayer player : this.getOnlinePlayers()) {
            player.getPlayer().sendTitle(title, Objects.requireNonNullElse(subtitle, ""),
                    Config.get(Plugin.GUILDS).getInt("broadcasts.guilds.fadeIn"),
                    Config.get(Plugin.GUILDS).getInt("broadcasts.guilds.stay"),
                    Config.get(Plugin.GUILDS).getInt("broadcasts.guilds.fadeOut"));
        }
    }

    public void disband() {
        // Update gPlayers & call event
        for (GPlayer onlineGuildMember : this.getOnlinePlayers()) {
            // Update gPlayer
            onlineGuildMember.leftGuild();

            // Call PlayerGuildChangeEvents
            GPlayerGuildChangedEvent gPlayerGuildChangedEvent = new GPlayerGuildChangedEvent(onlineGuildMember, this, null, GPlayerGuildChangedEvent.Reason.DISBAND);
            gPlayerGuildChangedEvent.runSync();
        }

        // Update board
        Board.get().getGuildClaims(this).forEach(GuildChunk::setWilderness);

        for (Guild guild : Guilds.get().getAll()) {
            // Remove this guild from other guild's relationWishes
            if (guild.hasRelationWishWith(this)) guild.removeRelationWishWith(this);

            // Remove this guild from other guild's raidedBy
            if (guild.getRaidedBy() == this) guild.setRaidedBy(null);
        }

        // Remove data
        GuildData.get().remove(this);

        // Remove Guild from Guilds
        Guilds.get().remove(this);

        // Remove Guild from Indexes
        Indexing.get().remove(this);
    }

    public void setHome(Location home) {
        this.home = home;
        changed();
    }

    public boolean hasHome() {
        return this.home != null;
    }

    public void setRelationWish(Guild to, Relation wish) {
        relationWishes.put(to, wish);
        changed();
    }

    public Relation getRelationWishWith(Guild with) {
        return relationWishes.getOrDefault(with, Relation.NEUTRAL);
    }

    public void removeRelationWishWith(Guild with) {
        relationWishes.remove(with);
        changed();
    }

    public boolean hasRelationWishWith(Guild with) {
        return relationWishes.containsKey(with);
    }

    public boolean hasOnlinePlayer() {
        return !getOnlinePlayers().isEmpty();
    }

    @Override
    public Relation getRelationTo(RelationParticipator participator) {
        return rUtil.getRelation(this, participator);
    }

    @Override
    public String describe(RelationParticipator participator) {
        // All descriptions from guild will be bold because they will be used in guild announcements
        return rUtil.describeBold(this, participator);
    }

    public GuildRank getRankFor(GuildPermission permission) {
        return permissions.get(permission);
    }

    public boolean canClaim() {
        return getNumberOfClaims() < getPower();
    }

    public int getNumberOfClaims() {
        return Board.get().getGuildClaims(this).size();
    }

    public boolean hasClaim() {
        return !Board.get().getGuildClaims(this).isEmpty();
    }

    private void changed() {
        // Save Guild data
        GuildData.get().save(this);
    }

    public Invitation getInvite(GPlayer gPlayer) {
        UUID uuid = gPlayer.getUUID();
        for (Invitation invite : invites) {
            if (invite.getPlayerUUID().equals(uuid)) return invite;
        }
        return null;
    }
}