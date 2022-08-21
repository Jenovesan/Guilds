package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class Raiding implements Listener {

    private static Raiding i = new Raiding();
    public static Raiding get() { return i; }

    @EventHandler
    public void checkIfGuildBecomesRaidable(PlayerLosePowerEvent event) {

        if (event.getKiller() == null) return;

        gPlayer player = event.getPlayer();
        gPlayer killer = event.getKiller();
        Guild playerGuild = player.getGuild();
        Guild killerGuild = killer.getGuild();

        if (playerGuild == null || killerGuild == null) return;

        // Guild has more claims than power
        if (!playerGuild.isGettingRaided() && playerGuild.getNumberOfClaims() > playerGuild.getPower()) {
            int raidDuration = Config.get().getInt("raiding duration (min)");

            // Set raidable
            playerGuild.setRaidedBy(killerGuild);
            playerGuild.setRaidEndTime(util.getTimeLater(raidDuration));

            // Add to raidableGuilds
            getRaidedGuilds().add(playerGuild);

            // Broadcasts
            playerGuild.sendBroadcast(Messages.getMsg("broadcasts.raidable title"), Messages.getMsg("broadcasts.raidable subtitle", raidDuration));
            killerGuild.sendBroadcast(Messages.getMsg("broadcasts.raiding title", playerGuild), Messages.getMsg("broadcasts.raiding subtitle", raidDuration));
        }
    }

    private static HashSet<Guild> raidedGuilds = new HashSet<>();

    public static HashSet<Guild> getRaidedGuilds() {
        return raidedGuilds;
    }

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Guild guild : getRaidedGuilds()) {
                    long raidEndTime = guild.getRaidEndTime();
                    Guild raidingGuild = guild.getRaidedBy();
                    // Raid ends
                    if (currentTime >= raidEndTime) {
                        // Remove raid
                        guild.setRaidedBy(null);
                        raidedGuilds.remove(guild);

                        // Reset power
                        int playerMaxPower = Config.get().getInt("player max power");
                        for (gPlayer player : guild.getPlayers().keySet()) {
                            player.setPower(playerMaxPower);
                        }

                        // Send broadcasts
                        guild.sendBroadcast(Messages.getMsg("broadcasts.no longer raidable title"), null);
                        raidingGuild.sendBroadcast(Messages.getMsg("broadcasts.no longer raiding title", guild), null);
                    }
                }
            }
        }.runTaskTimerAsynchronously(GuildWars.getInstance(), 1200, 1200);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerChunkUpdateIfRaidInterfering(PlayerChunkUpdateEvent event) {
        gPlayer player = event.getPlayer();
        Guild chunkHost = event.getNewGuildChunk().getGuild();

        if (chunkHost == null) return;

        // Chunk host is being raided and the player's guild is not raiding them
        if (chunkHost.getRaidedBy() != null && chunkHost.getRaidedBy() != event.getPlayer().getGuild()) {
            event.setCancelled(true);
            player.sendFailMsg(Messages.getMsg("raid interfering", chunkHost));
        }
    }

    public void load() {
        for (Guild guild : Guilds.get().getAll()) {
            if (guild.isGettingRaided()) {
                getRaidedGuilds().add(guild);
            }
        }
    }
}
