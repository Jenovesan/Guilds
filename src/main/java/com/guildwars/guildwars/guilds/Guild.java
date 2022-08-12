package com.guildwars.guildwars.guilds;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void setName(gPlayer changer, String newName) {
        this.name = newName;
        this.sendAnnouncement(Messages.getMsg("guild announcements.name changed", changer, null, newName));
    }

    public void setDescription(gPlayer changer, String desc) {
        this.description = desc;
        this.sendAnnouncement(Messages.getMsg("guild announcements.description changed", changer, null, desc));
    }

    public void setLeader(gPlayer oldLeader, gPlayer newLeader) {
        // Set the new leader to leader
        this.getPlayers().replace(newLeader, GuildRank.LEADER);

        // Set the old leader to coleader
        this.getPlayers().replace(oldLeader, GuildRank.COLEADER);
        // Inform guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.gave leadership", oldLeader, newLeader, null));
    }

    public void invite(gPlayer inviter, gPlayer invitee) {

        // Convert player to offline player

        // Invite invitee
        getInvites().add(invitee);

        // Remove player invitation later
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if invitee still has an invitation
                if (!isInvited(invitee)) {
                    return;
                }

                // Remove player invitation
                getInvites().remove(invitee);

                // Notify player if they're online
                if (invitee.getPlayer() != null) {
                    invitee.sendNotifyMsg(Messages.getMsg("commands.invite.invite expired", inviter, invitee, null));
                }
            }
        }.runTaskLaterAsynchronously(GuildWars.getInstance(), Config.get().getInt("invite expire time (s)") * 20L);
    }

    public void deInvite(gPlayer invitee) {
        getInvites().remove(invitee);
    }

    public void addPlayer(gPlayer newPlayer) {

        GuildRank playerNewGuidRank = GuildRank.valueOf(Config.get().getString("join guild at rank"));

        // Send Guild Announcement
        // This is done first because if it was done after the player was added to the guild, the new player would receive this announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player join", newPlayer, newPlayer, null));

        // Update Guild's player list
        this.getPlayers().put(newPlayer, playerNewGuidRank);

        // Update gPlayer
        newPlayer.joinedNewGuild(this);
    }

    public void removePlayer(gPlayer player) {

        // Update Guild's player list
        this.getPlayers().remove(player);

        // Update gPlayer
        player.leftGuild();

        // Send Guild Announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player leave", player, player, null));
    }

    public void kickPlayer(gPlayer kicker, gPlayer kickee) {

        // Remove player from Guild
        this.getPlayers().remove(kickee);

        // Update gPlayer if kickee is online
        kickee.leftGuild();

        // Send Guild Announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.player kicked", kicker, kickee, null));
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

    public void disband(gPlayer player) {

        // Remove Guild from Guilds
        Guilds.removeGuild(this);

        // Remove Guild Data
        Guilds.removeGuildData(this.getId());

        // Update gPlayers & call event
        for (gPlayer onlineGuildMember : this.getOnlinePlayers()) {
            // Update gPlayer
            onlineGuildMember.leftGuild();
            // Call PlayerGuildChangeEvents
            Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(onlineGuildMember, null, PlayerGuildChangeEvent.Reason.DISBAND));
        }

        // Update board
        for (int[] claimBoardLocation : this.getClaimLocations()) {
            Board.getBoard()[claimBoardLocation[0]][claimBoardLocation[1]].setWilderness();
        }

        // Send Guild Announcement
        sendAnnouncement(Messages.getMsg("guild announcements.disband", player, null, null));
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

    public void enemy(gPlayer player, Guild enemyGuild) {
        // Add enemyGuild to this enemy list
        this.getEnemies().add(enemyGuild.getId());

        // Add this guild to enemyGuild enemy list
        enemyGuild.getEnemies().add(this.getId());

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.enemied guild", player, player, enemyGuild.getName()));

        // Inform enemyGuild
        enemyGuild.sendAnnouncement(Messages.getMsg("guild announcements.guild has enemied your guild", player, player, this.getName()));
    }

    public void truce(Guild enemyGuild) {
        // Remove enemyGuild from this enemy list
        this.getEnemies().remove(enemyGuild.getId());

        // Remove this guild from enemyGuild enemy list
        enemyGuild.getEnemies().remove(this.getId());

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", null, null, enemyGuild.getName()));

        // Inform enemyGuild
        enemyGuild.sendAnnouncement(Messages.getMsg("guild announcements.truced guild", null, null, this.getName()));

        // Remove enemyGuild's truce request with this guild
        enemyGuild.getTruceRequests().remove(this.getId());
    }

    public void sendTruceRequest(gPlayer player, Guild guildToTruce) {
        // Add to truce requests
        this.getTruceRequests().add(guildToTruce.getId());

        // Inform Guild to truce
        guildToTruce.sendAnnouncement(Messages.getMsg("guild announcements.received truce request", player, player, this.getName()));

        // Inform this guild
        this.sendAnnouncement(Messages.getMsg("guild announcements.sent truce request", player, player, guildToTruce.getName()));

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

    public void claim(gPlayer claimer, GuildChunk chunk) {
        // Claim chunk on Board
        chunk.claim(this);

        // Add chunk to this Guild's claim locations list
        this.getClaimLocations().add(chunk.getBoardLocation());

        // Send Guild announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.claimed land", claimer, null, null));
    }

    public void unclaim(gPlayer unclaimer, GuildChunk chunk) {
        // Unclaim chunk on Board
        chunk.setWilderness();

        // Remove chunk from this Guild's claim locations list
        this.getClaimLocations().remove(chunk.getBoardLocation());

        // Send Guild announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.unclaimed land", unclaimer, null, null));
    }

    public void unclaimAll(gPlayer unclaimer) {
        // Update Board
        for (int[] claimBoardLocation : this.getClaimLocations()) {
            Board.getBoard()[claimBoardLocation[0]][claimBoardLocation[1]].setWilderness();
        }

        // Send Guild announcement
        this.sendAnnouncement(Messages.getMsg("guild announcements.unclaimed all", unclaimer, null, null));
    }
}





























