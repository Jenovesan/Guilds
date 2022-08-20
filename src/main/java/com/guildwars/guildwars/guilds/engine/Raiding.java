package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.GuildsIndex;
import com.guildwars.guildwars.guilds.event.GuildRaidEndEvent;
import com.guildwars.guildwars.guilds.event.GuildRaidStartEvent;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class Raiding implements Listener {

    @EventHandler
    public void checkIfGuildBecomesRaidable(PlayerLosePowerEvent event) {

        if (event.getKiller() == null) return;

        gPlayer player = event.getPlayer();
        gPlayer killer = event.getKiller();
        Guild playerGuild = player.getGuild();
        Guild killerGuild = killer.getGuild();

        if (playerGuild == null || killerGuild == null) return;

        // Guild has more claims than power
        if (!killerGuild.isRaiding() && playerGuild.getNumberOfClaims() > playerGuild.getPower()) {
            int raidDuration = Config.get().getInt("raiding duration (min)");

            // Set raidable
            killerGuild.setRaid(playerGuild.getId());
            killerGuild.setRaidEndTime(util.getTimeLater(raidDuration));

            // Add to raidableGuilds
            getRaidingGuilds().add(killerGuild);

            // Call event
            Bukkit.getServer().getPluginManager().callEvent(new GuildRaidStartEvent(playerGuild, killerGuild));

            // Broadcasts
            playerGuild.sendBroadcast(Messages.getMsg("broadcasts.raidable title"), Messages.getMsg("broadcasts.raidable subtitle", raidDuration));
            killerGuild.sendBroadcast(Messages.getMsg("broadcasts.raiding title", playerGuild), Messages.getMsg("broadcasts.raiding subtitle", raidDuration));
        }
    }

    private static HashSet<Guild> raidingGuilds = new HashSet<>();

    public static HashSet<Guild> getRaidingGuilds() {
        return raidingGuilds;
    }

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Guild guild : getRaidingGuilds()) {
                    long raidEndTime = guild.getRaidEndTime();
                    int raidedGuildId = guild.getRaid();
                    // Raid ends
                    if (currentTime >= raidEndTime) {
                        // Remove raid
                        guild.setRaid(0);
                        raidingGuilds.remove(guild);

                        // Reset power
                        Guild raidedGuild = GuildsIndex.getGuildById(raidedGuildId);
                        int playerMaxPower = Config.get().getInt("player max power");
                        for (gPlayer player : raidedGuild.getPlayers().keySet()) {
                            player.setPower(playerMaxPower);
                        }

                        // Call event
                        Bukkit.getServer().getPluginManager().callEvent(new GuildRaidEndEvent(raidedGuild, guild));

                        // Send broadcasts
                        raidedGuild.sendBroadcast(Messages.getMsg("broadcasts.no longer raidable title"), null);
                        guild.sendBroadcast(Messages.getMsg("broadcasts.no longer raiding title", raidedGuild), null);
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 1200, 1200);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerChunkUpdateIfRaidInterfering(PlayerChunkUpdateEvent event) {
        int newChunkHostId = event.getNewGuildChunk().getGuild().getId();

        if (newChunkHostId == 0) return;

        int playerGuildId = event.getPlayer().getGuildId();

        if (playerGuildId == newChunkHostId) return;

        Integer newChunkHostRaider = GuildsIndex.getRaids().get(newChunkHostId);
        // Chunk Host is being raided
        if (newChunkHostRaider != null) {
            // Chunk host is being raided by a different guild
            if (newChunkHostRaider != playerGuildId) {
                event.setCancelled(true);
            }
        }
    }

    public static void load() {
        for (Guild guild : Guilds.getAllGuilds()) {
            if (guild.isRaiding()) {
                getRaidingGuilds().add(guild);
            }
        }
    }
}
