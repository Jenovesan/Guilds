package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Guild {

    private final String id;
    private String name;
    private String description;
    private HashSet<gPlayer> players = new HashSet<>();
    private HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
    private HashSet<gPlayer> invites = new HashSet<>();
    private HashSet<Guild> enemies = new HashSet<>();
    private HashSet<Guild> truceRequests = new HashSet<>();
    private HashSet<int[]> claimLocations = new HashSet<>();
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

    public HashSet<gPlayer> getPlayers() {
        return players;
    }

    public HashMap<GuildPermission, GuildRank> getPermissions() {
        return permissions;
    }

    public HashSet<gPlayer> getInvites() {
        return invites;
    }

    public HashSet<Guild> getEnemies() {
        return enemies;
    }

    public HashSet<Guild> getTruceRequests() {
        return truceRequests;
    }

    public HashSet<int[]> getClaimLocations() {
        return claimLocations;
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

    // Creating a new guild
    public Guild(gPlayer creator, String name, String description) {
        this.id = gUtil.getNewUUID();
        this.name = name;
        this.description = description;
        if (creator != null) {
            this.getPlayers().add(creator);
        }
        loadDefaults();
    }

    // Loading an existing guild on startup
    public Guild(String id,
                 String name,
                 String description,
                 HashSet<gPlayer> players,
                 HashMap<GuildPermission, GuildRank> permissions,
                 HashSet<int[]> claimLocations,
                 long raidEndTime,
                 Location home) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.players = players;
        this.permissions = permissions;
        this.claimLocations = claimLocations;
        this.raidEndTime = raidEndTime;
        this.home = home;
    }

    public void loadDefaults() {
        // Permissions
        this.permissions.put(GuildPermission.INVITE, GuildRank.valueOf(Config.get().getString("default permissions.invite")));
        this.permissions.put(GuildPermission.SET_DESC, GuildRank.valueOf(Config.get().getString("default permissions.set_desc")));
        this.permissions.put(GuildPermission.SET_NAME, GuildRank.valueOf(Config.get().getString("default permissions.set_name")));
        this.permissions.put(GuildPermission.CHAT, GuildRank.valueOf(Config.get().getString("default permissions.chat")));
        this.permissions.put(GuildPermission.RELATIONS, GuildRank.valueOf(Config.get().getString("default permissions.relations")));
        this.permissions.put(GuildPermission.CLAIM, GuildRank.valueOf(Config.get().getString("default permissions.claim")));
        this.permissions.put(GuildPermission.UNCLAIM, GuildRank.valueOf(Config.get().getString("default permissions.unclaim")));
        this.permissions.put(GuildPermission.UNCLAIM_ALL, GuildRank.valueOf(Config.get().getString("default permissions.unclaim_all")));
        this.permissions.put(GuildPermission.SETHOME, GuildRank.valueOf(Config.get().getString("default permissions.sethome")));
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void invite(gPlayer invitee) {
        getInvites().add(invitee);
    }

    public void removeInvite(gPlayer invitee) {
        getInvites().remove(invitee);
    }

    public void addPlayer(gPlayer newPlayer) {
        this.getPlayers().add(newPlayer);
    }

    public void removePlayer(gPlayer player) {
        this.getPlayers().remove(player);
    }

    public HashSet<gPlayer> getOnlinePlayers() {
        HashSet<gPlayer> onlinePlayers = new HashSet<>();
        for (gPlayer player : this.getPlayers()) {
            Player onlinePlayer = player.getPlayer();
            if (onlinePlayer != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public boolean isInvited(gPlayer player) {
        return this.getInvites().contains(player);
     }

    public boolean isFull() {
        int maxPlayersInGuild = Config.get().getInt("max players in guild");
        return this.getPlayers().size() >= maxPlayersInGuild;
    }

    public void sendAnnouncement(String msg) {
        for (gPlayer onlinePlayer : this.getOnlinePlayers()) {
            onlinePlayer.sendNotifyMsg(msg);
        }
    }

    public boolean isEnemied(Guild guild) {
        return this.getEnemies().contains(guild);
    }

    public void addEnemy(Guild enemyGuild) {
        this.getEnemies().add(enemyGuild);
    }

    public void truce(Guild enemyGuild) {
        this.getEnemies().remove(enemyGuild);
    }

    public void removeTruceRequest(Guild guild) {
        this.getTruceRequests().remove(guild);
    }

    public void sendTruceRequest(Guild guildToTruce) {
        this.getTruceRequests().add(guildToTruce);
    }

    public boolean hasTruceRequestWith(Guild guild) {
        return this.getTruceRequests().contains(guild);
    }

    public int getPower() {
        int power = 0;
        for (gPlayer player : this.getPlayers()) {
            power += Math.floor(player.getPower());
        }
        return power;
    }

    public int getNumberOfClaims() {
        return this.getClaimLocations().size();
    }

    public int getMaxPower() {
        return this.getPlayers().size() * Config.get().getInt("player max power");
    }

    public boolean canClaim() {
        return this.getPower() > this.getNumberOfClaims();
    }

    public void claim(GuildChunk chunk) {
        // Add chunk to this Guild's claim locations list
        this.getClaimLocations().add(chunk.getBoardLocation());
    }

    public void unclaim(GuildChunk chunk) {
        this.getClaimLocations().remove(chunk.getBoardLocation());
    }

    public int getExcessPower() {
        return this.getPower() - this.getNumberOfClaims();
    }

    public void setRaidedBy(Guild guild) {
        this.raidedBy = guild;
    }

    public void setRaidEndTime(long time) {
        this.raidEndTime = time;
    }

    public boolean isGettingRaided() {
        return this.getRaidedBy() != null;
    }

    public void sendBroadcast(String title, String subtitle) {
        for (gPlayer player : this.getOnlinePlayers()) {
            player.getPlayer().sendTitle(title, Objects.requireNonNullElse(subtitle, ""), Config.get().getInt("broadcasts.guilds.fadeIn"), Config.get().getInt("broadcasts.guilds.stay"), Config.get().getInt("broadcasts.guilds.fadeOut"));
        }
    }

    public void disband() {
        // Remove data
        GuildData.get().remove(this);

        // Remove Guild from Guilds
        Guilds.get().remove(this);

        // Update gPlayers & call event
        for (gPlayer onlineGuildMember : this.getOnlinePlayers()) {
            // Update gPlayer
            onlineGuildMember.leftGuild();

            // Call PlayerGuildChangeEvents
            PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(onlineGuildMember, null, PlayerGuildChangeEvent.Reason.DISBAND);
            playerGuildChangeEvent.run();
        }

        // Update board
        for (int[] claimBoardLocation : this.getClaimLocations()) {
            Board.getBoard()[claimBoardLocation[0]][claimBoardLocation[1]].setWilderness();
        }

        // Remove index
        GuildsIndex.get().remove(this);
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public boolean hasHome() {
        return this.home != null;
    }
}