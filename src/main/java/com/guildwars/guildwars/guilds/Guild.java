package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Guild {

    private final int id;
    private String name;
    private String description;
    private HashMap<UUID, GuildRank> players = new HashMap<>();
    private HashMap<GuildPermission, GuildRank> permissions = new HashMap<>();
    private HashSet<OfflinePlayer> invites = new HashSet<>();
    private HashSet<Integer> enemies = new HashSet<>();
    private HashSet<Integer> truceRequests = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<UUID, GuildRank> getPlayers() {
        return players;
    }

    public HashMap<GuildPermission, GuildRank> getPermissions() {
        return permissions;
    }

    public HashSet<OfflinePlayer> getInvites() {
        return invites;
    }

    public HashSet<Integer> getEnemies() {
        return enemies;
    }

    public HashSet<Integer> getTruceRequests() {
        return truceRequests;
    }

    // Creating a new guild
    public Guild(UUID creatorUUID, String name, String description) {
        this.id = Guilds.getNewGuildId();
        this.name = name;
        this.description = description;
        this.getPlayers().put(creatorUUID, GuildRank.LEADER);
        loadDefaults();
        Guilds.addGuild(this);
    }

    // Loading an existing guild on startup
    public Guild(Integer id,
                 String name,
                 String description,
                 HashMap<UUID, GuildRank> players,
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
    }

    public void setName(Player changer, String newName) {
        this.name = newName;
        this.sendAnnouncement(Messages.getMsg("guild announcements.name changed", changer, null, new String[] {newName}, this, this, this.getRank(changer.getUniqueId()), null));
    }

    public void setDescription(Player changer, String desc) {
        this.description = desc;
        this.sendAnnouncement(Messages.getMsg("guild announcements.description changed", changer, null, new String[] {desc}, this, this, this.getRank(changer.getUniqueId()), null));
    }

    public void setLeader(Player oldLeader, OfflinePlayer newLeader) {
        // Set the new leader to leader
        this.getPlayers().replace(newLeader.getUniqueId(), GuildRank.LEADER);

        // Set the old leader to coleader
        this.getPlayers().replace(oldLeader.getUniqueId(), GuildRank.COLEADER);
        // Inform guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.gave leadership", oldLeader, newLeader.getPlayer(), null, this, this, GuildRank.LEADER, null));
    }

    public void invite(Player inviter, Player inviteePlayer) {

        // Convert player to offline player
        OfflinePlayer invitee = Bukkit.getOfflinePlayer(inviteePlayer.getUniqueId());

        // Invite invitee
        getInvites().add(invitee);

        // Remove player invitation later
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if invitee still has an invitation
                if (!isInvited(inviteePlayer)) {
                    return;
                }

                // Remove player invitation
                getInvites().remove(invitee);

                // Notify player if they're online
                if (inviteePlayer.isOnline()) {
                    pUtil.sendNotifyMsg(inviteePlayer, Messages.getMsg("commands.invite.invite expired", inviter, inviteePlayer, null, get(), get(), getRank(inviter.getUniqueId()),  GuildRank.valueOf(Config.get().getString("join guild at rank"))));
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("invite expire time (s)") * 20L);
    }

    public void deInvite(OfflinePlayer invitee) {
        getInvites().remove(invitee);
    }

    public void addPlayer(Player player) {

        GuildRank playerNewGuidRank = GuildRank.valueOf(Config.get().getString("join guild at rank"));

        // Send Guild Announcement
        // This is done first because if it was done after the player was added to the guild, the new player would receive this announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player join", player, player, null, get(), get(), playerNewGuidRank,  playerNewGuidRank));

        // Update Guild's player list
        this.getPlayers().put(player.getUniqueId(), playerNewGuidRank);

        // Update gPlayer
        gPlayer gPlayer = gPlayers.get(player);
        gPlayer.joinedNewGuild(this);
    }

    public void removePlayer(Player player) {

        // Update Guild's player list
        this.getPlayers().remove(player.getUniqueId());

        // Update gPlayer
        gPlayer gPlayer = gPlayers.get(player);
        gPlayer.leftGuild();

        // Send Guild Announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player leave", player, player, null, get(), null, null,  null));
    }

    public void kickPlayer(Player kicker, OfflinePlayer kickee) {

        // Remove player from Guild
        this.getPlayers().remove(kickee.getUniqueId());

        // Update gPlayer if kickee is online
        Player kickeePlayer = kickee.getPlayer();
        if (kickeePlayer != null) {
            gPlayer gPlayer = gPlayers.get(kickeePlayer);
            gPlayer.leftGuild();
        }

        // Send Guild Announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player kicked", kicker, kickeePlayer, null, get(), null, null,  null));
    }

    public GuildRank getRank(UUID uuid) {
        return this.getPlayers().get(uuid);
    }

    public HashSet<Player> getOnlinePlayers() {
        HashSet<Player> onlinePlayers = new HashSet<>();
        for (UUID uuid : this.getPlayers().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public HashSet<Player> getOnlinePlayersThatHavePermission(GuildPermission permission) {
        int minGuildRankLevel = this.getPermissions().get(permission).level;

        HashSet<Player> onlinePlayers = new HashSet<>();
        for (Map.Entry<UUID, GuildRank> entry : this.getPlayers().entrySet()) {
            // GuildRank too low
            if (entry.getValue().level < minGuildRankLevel) {
                continue;
            }

            // Add player if they are online
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public void disband(Player player) {

        // Remove Guild from Guilds
        Guilds.removeGuild(this);

        // Remove Guild Data
        Guilds.removeGuildData(this.getId());

        // Send Guild Announcement
        sendAnnouncement(Messages.getMsg("guild announcements.disband", player, null, null, this, null, GuildRank.LEADER, null));
    }

    public boolean isInvited(Player player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
        return this.getInvites().contains(offlinePlayer);
     }

    public OfflinePlayer getInvitedPlayer(String name) {
        for (OfflinePlayer player : this.getInvites()) {
            String playerName = player.getName();
            if (playerName != null && playerName.equalsIgnoreCase(name)) {
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
        for (Player onlinePlayer : this.getOnlinePlayers()) {
            pUtil.sendNotifyMsg(onlinePlayer, msg);
        }
    }

    public OfflinePlayer getOfflinePlayer (String name) {
        for (UUID uuid : this.getPlayers().keySet()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public GuildRank getGuildRank(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        return this.getPlayers().get(uuid);
    }

    public void changeGuildRank(OfflinePlayer player, GuildRank newRank) {
        UUID uuid = player.getUniqueId();
        this.getPlayers().replace(uuid, newRank);
    }

    public boolean isEnemied(Guild guild) {
        return this.getEnemies().contains(guild.getId());
    }

    public void enemy(Player player, Guild enemyGuild) {
        // Add enemyGuild to this enemy list
        this.getEnemies().add(enemyGuild.getId());

        // Add this guild to enemyGuild enemy list
        enemyGuild.getEnemies().add(this.getId());

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.enemied guild", player, null, null, this, enemyGuild, getRank(player.getUniqueId()), null));

        // Inform enemyGuild
        enemyGuild.sendAnnouncement(Messages.getMsg("guild announcements.guild has enemied your guild", player, player, null, enemyGuild, this, getRank(player.getUniqueId()), getRank(player.getUniqueId())));
    }

    public void truce(Guild enemyGuild) {
        // Remove enemyGuild from this enemy list
        this.getEnemies().remove(enemyGuild.getId());

        // Remove this guild from enemyGuild enemy list
        enemyGuild.getEnemies().remove(this.getId());

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", null, null, null, this, enemyGuild, null, null));

        // Inform enemyGuild
        enemyGuild.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", null, null, null, enemyGuild, this, null, null));

        // Remove enemyGuild's truce request with this guild
        enemyGuild.getTruceRequests().remove(this.getId());
    }

    public void sendTruceRequest(Player player, Guild guildToTruce) {
        // Add to truce requests
        this.getTruceRequests().add(guildToTruce.getId());

        // Inform Guild to truce
        guildToTruce.sendAnnouncement(Messages.getMsg("guild announcements.received truce request", player, player, null, guildToTruce, this, getRank(player.getUniqueId()), null));

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.sent truce request", player, player, null, this, guildToTruce, getRank(player.getUniqueId()), null));

        // Remove truce request later
        new BukkitRunnable() {
            @Override
            public void run() {
                // Remove truce request
                getTruceRequests().remove(guildToTruce.getId());
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("truce request expire time (m)") * 1200L);
    }

    public boolean hasTruceRequestWith(Guild guild) {
        return this.getTruceRequests().contains(guild.getId());
    }

    public Guild get() {
        return this;
    }
}





























