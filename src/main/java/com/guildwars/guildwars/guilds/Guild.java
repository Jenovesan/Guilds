package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.guilds.files.Config;
import org.bukkit.entity.Player;

import java.util.*;

public class Guild {

    private final int id;
    private String name;
    private String description;
    private HashMap<gPlayer, GuildRank> players = new HashMap<>();
    private HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
    private HashSet<gPlayer> invites = new HashSet<>();
    private HashSet<Integer> enemies = new HashSet<>();
    private HashSet<Integer> truceRequests = new HashSet<>();
    private HashSet<int[]> claimLocations = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<gPlayer, GuildRank> getPlayers() {
        return players;
    }

    public HashMap<GuildPermission, GuildRank> getPermissions() {
        return permissions;
    }

    public HashSet<gPlayer> getInvites() {
        return invites;
    }

    public HashSet<Integer> getEnemies() {
        return enemies;
    }

    public HashSet<Integer> getTruceRequests() {
        return truceRequests;
    }

    public HashSet<int[]> getClaimLocations() {
        return claimLocations;
    }

    // Creating a new guild
    public Guild(gPlayer creator, String name, String description) {
        this.id = Guilds.getNewGuildId();
        this.name = name;
        this.description = description;
        this.getPlayers().put(creator, GuildRank.LEADER);
        loadDefaults();
        Guilds.addGuild(this);
    }

    // Loading an existing guild on startup
    public Guild(Integer id,
                 String name,
                 String description,
                 HashMap<gPlayer, GuildRank> players,
                 HashMap<GuildPermission, GuildRank> permissions,
                 HashSet<Integer> enemies) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.players = players;
        this.permissions = permissions;
        this.enemies = enemies;
        Guilds.addGuild(this);
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
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setLeader(gPlayer oldLeader, gPlayer newLeader) {
        // Set the new leader to leader
        this.getPlayers().replace(newLeader, GuildRank.LEADER);

        // Set the old leader to coleader
        this.getPlayers().replace(oldLeader, GuildRank.COLEADER);
    }

    public void invite(gPlayer invitee) {
        getInvites().add(invitee);
    }

    public void removeInvite(gPlayer invitee) {
        getInvites().remove(invitee);
    }

    public void addPlayer(gPlayer newPlayer) {
        GuildRank playerNewGuidRank = GuildRank.valueOf(Config.get().getString("join guild at rank"));
        this.getPlayers().put(newPlayer, playerNewGuidRank);
    }

    public void removePlayer(gPlayer player) {
        this.getPlayers().remove(player);
    }

    public void kickPlayer(gPlayer kickee) {
        this.getPlayers().remove(kickee);
    }

    public GuildRank getRank(gPlayer player) {
        return this.getPlayers().get(player);
    }

    public HashSet<gPlayer> getOnlinePlayers() {
        HashSet<gPlayer> onlinePlayers = new HashSet<>();
        for (gPlayer player : this.getPlayers().keySet()) {
            Player onlinePlayer = player.getPlayer();
            if (onlinePlayer != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public HashSet<Player> getOnlinePlayersThatHavePermission(GuildPermission permission) {
        int minGuildRankLevel = this.getPermissions().get(permission).level;

        HashSet<Player> onlinePlayers = new HashSet<>();
        for (Map.Entry<gPlayer, GuildRank> entry : this.getPlayers().entrySet()) {
            // GuildRank too low
            if (entry.getValue().level < minGuildRankLevel) {
                continue;
            }

            // Add player if they are online
            Player onlinePlayer = entry.getKey().getPlayer();
            if (onlinePlayer != null) {
                onlinePlayers.add(onlinePlayer);
            }
        }
        return onlinePlayers;
    }

    public boolean isInvited(gPlayer player) {
        return this.getInvites().contains(player);
     }

    public gPlayer getInvitedPlayer(String name) {
        for (gPlayer player : this.getInvites()) {
            String playerName = player.getName();
            if (playerName.equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
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

    public GuildRank getGuildRank(gPlayer player) {
        return this.getPlayers().get(player);
    }

    public void changeGuildRank(gPlayer player, GuildRank newRank) {
        this.getPlayers().replace(player, newRank);
    }

    public boolean isEnemied(Guild guild) {
        return this.getEnemies().contains(guild.getId());
    }

    public void enemy(Guild enemyGuild) {
        this.getEnemies().add(enemyGuild.getId());
    }

    public void truce(Guild enemyGuild) {
        this.getEnemies().remove(enemyGuild.getId());
    }

    public void removeTruceRequest(Guild guild) {
        this.getTruceRequests().remove(guild.getId());
    }

    public void sendTruceRequest(Guild guildToTruce) {
        this.getTruceRequests().add(guildToTruce.getId());
    }

    public boolean hasTruceRequestWith(Guild guild) {
        return this.getTruceRequests().contains(guild.getId());
    }

    public Guild get() {
        return this;
    }

    public gPlayer getPlayer(String name) {
        for (gPlayer player : this.getPlayers().keySet()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public int getPower() {
        int power = 0;
        for (gPlayer player : this.getPlayers().keySet()) {
            power += player.getPower();
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

    public void unclaimAll() {
        this.getClaimLocations().clear();
    }

    public int getExcessPower() {
        return this.getPower() - this.getNumberOfClaims();
    }

    public boolean isOverclaimable() {
        return this.getNumberOfClaims() > this.getPower();
    }

    public void removeClaim(GuildChunk chunk) {
        this.getClaimLocations().remove(chunk.getBoardLocation());
    }
}