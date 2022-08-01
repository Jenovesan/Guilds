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
    private HashMap<UUID, Rank> players = new HashMap<>();
    private HashMap<Permission, Rank> permissions = new HashMap<>();
    private HashSet<OfflinePlayer> invites = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<UUID, Rank> getPlayers() {
        return players;
    }

    public HashMap<Permission, Rank> getPermissions() {
        return permissions;
    }

    public HashSet<OfflinePlayer> getInvites() {
        return invites;
    }

    // Creating a new guild
    public Guild(UUID creatorUUID, String name, String description) {
        this.id = Guilds.getNewGuildId();
        this.name = name;
        this.description = description;
        this.setLeader(creatorUUID);
        loadDefaults();
        Guilds.addGuild(this);
    }

    // Loading an existing guild on startup
    public Guild(Integer id, String name, String description, HashMap<UUID, Rank> players,HashMap<Permission, Rank> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.players = players;
        this.permissions = permissions;
        Guilds.addGuild(this);
    }

    public void loadDefaults() {
        // Permissions
        this.permissions.put(Permission.INVITE, Rank.valueOf(Config.get().getString("default permissions.invite")));
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setRank(Object playerInfo, Rank rank) {
        if (playerInfo instanceof Player) {
            this.players.replace(((Player) playerInfo).getUniqueId(), rank);
        }
        else if (playerInfo instanceof UUID) {
            this.players.replace((UUID) playerInfo, rank);
        }
    }

    public void setLeader(UUID uuid) {
        boolean hasLeader = this.getPlayers().containsValue(Rank.LEADER);
        if (!hasLeader) {
            this.getPlayers().put(uuid, Rank.LEADER);
        }
        // Add else clause
    }

    public void invite(Player inviteePlayer) {

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
                    pUtil.sendNotifyMsg(inviteePlayer, Messages.getMsg("commands.invite.invite expired").replace("<guild>", getName()));
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("invite expire time (s)") * 20L);
    }

    public void deInvite(OfflinePlayer invitee) {
        getInvites().remove(invitee);
    }

    public void addPlayer(Player player) {

        // Send Guild Announcement
        // This is done first because if it was done after the player was added to the guild, the new player would receive this announcement
        this.sendAnnouncement(Messages.getMsg("guilds.announcements.player join").replace("<name>", player.getName()));

        // Update Guild's player list
        this.getPlayers().put(player.getUniqueId(), Rank.valueOf(Config.get().getString("join guild at rank")));

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
        this.sendAnnouncement(Messages.getMsg("guilds.announcements.player leave").replace("<name>", player.getName()));
    }

    public static enum Rank {
        NONE(0),
        RECRUIT(1),
        MEMBER(2),
        MOD(3),
        COLEADER(4),
        LEADER(5);

        public final int level;
        Rank (int level) {
            this.level = level;
        }
    }

    public static enum Permission {
        INVITE
    }

    public Guild.Rank getRank(UUID uuid) {
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

    public void disband() {

        // Update Guild members
        for (Player onlinePlayer : this.getOnlinePlayers()) {
            gPlayers.get(onlinePlayer).leftGuild();
        }

        // Remove Guild from Guilds
        Guilds.removeGuild(this);

        // Remove Guild Data
        Guilds.removeGuildData(this.getId());

        // Send Guild Announcement
        sendAnnouncement(Messages.getMsg("guilds.announcements.disband"));
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
}

