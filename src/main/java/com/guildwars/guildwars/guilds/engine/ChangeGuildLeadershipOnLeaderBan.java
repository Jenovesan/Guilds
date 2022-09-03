package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildRank;
import com.guildwars.guildwars.guilds.event.GPlayerQuitEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.gPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ChangeGuildLeadershipOnLeaderBan extends Engine{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBan(GPlayerQuitEvent event) {
        gPlayer player = event.getGPlayer();
        Player playerPlayer = player.getPlayer();
        if (!playerPlayer.isBanned()) return;

        // Player has been banned

        if (player.getGuildRank() != GuildRank.LEADER) return;

        // Player was leader of a guild

        Guild guild = player.getGuild();

        // Change guild leadership
        gPlayer newLeader = getNewLeader(guild);

        // Guild does not have any other members
        if (newLeader == null) {
            guild.disband();
        }
        // Guild has members
        else {
            newLeader.setGuildRank(GuildRank.LEADER);
            player.setGuildRank(GuildRank.COLEADER);

            // Save data
            PlayerData.get().save(newLeader);
            PlayerData.get().save(player);

            // Inform guild
            guild.sendAnnouncement(Messages.getMsg("guild announcements.gave leadership", player, newLeader));
        }
    }

    private gPlayer getNewLeader(Guild guild) {
        // Iterate through all ranks, except for leader, incase banee's guild does not have a coleader, mod, etc.
        // Essentially iterate through all players starting from highest rank and going to lowest rank.
        GuildRank[] guildRanks = GuildRank.getAll();
        for (int i = guildRanks.length-2; i > -1; i--) {
            GuildRank rank = guildRanks[i];
            System.out.println(rank.name());

            for (gPlayer member : guild.getPlayers()) {
                if (member.getGuildRank() == rank) {
                    return member;
                }
            }
        }
        return null;
    }
}
