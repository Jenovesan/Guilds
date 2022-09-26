package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.entity.GuildChunk;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.event.PlayerLosePowerEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.PlayerData;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.utils.util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Raiding extends Engine {

    public Raiding() {
        super(Config.get(Plugin.GUILDS).getLong("raiding kick non-raiders update (tick)"));
    }

    @EventHandler
    public void checkIfGuildBecomesRaidable(PlayerLosePowerEvent event) {

        if (event.getKiller() == null) return;

        GPlayer player = event.getPlayer();
        GPlayer killer = event.getKiller();
        Guild playerGuild = player.getGuild();
        Guild killerGuild = killer.getGuild();

        if (playerGuild == null || killerGuild == null) return;

        // Guild has more claims than power
        if (!playerGuild.isGettingRaided() && playerGuild.getNumberOfClaims() > playerGuild.getPower()) {
            int raidDuration = Config.get(Plugin.GUILDS).getInt("raiding duration (min)");

            // Set raidable
            playerGuild.setRaidedBy(killerGuild);
            playerGuild.setRaidEndTime(util.getTimeLater(raidDuration));

            // Save data
            GuildData.get().save(playerGuild);

            // Broadcasts
            playerGuild.sendBroadcast(Messages.get(Plugin.GUILDS).get("broadcasts.raidable title"), Messages.get(Plugin.GUILDS).get("broadcasts.raidable subtitle", raidDuration));
            killerGuild.sendBroadcast(Messages.get(Plugin.GUILDS).get("broadcasts.raiding title", playerGuild), Messages.get(Plugin.GUILDS).get("broadcasts.raiding subtitle", raidDuration));
        }
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Guild guild : Guilds.get().getAll()) {
            if (!guild.isGettingRaided()) continue;

            long raidEndTime = guild.getRaidEndTime();
            Guild raidingGuild = guild.getRaidedBy();
            // Raid ends
            if (currentTime >= raidEndTime) {
                // Remove raid
                guild.setRaidedBy(null);

                // Save data
                GuildData.get().save(guild);

                // Reset power
                int playerMaxPower = Config.get(Plugin.GUILDS).getInt("player max power");
                for (GPlayer player : guild.getPlayers()) {
                    player.setPower(playerMaxPower);
                    PlayerData.get().save(player);
                }

                // Send broadcasts
                guild.sendBroadcast(Messages.get(Plugin.GUILDS).get("broadcasts.no longer raidable title"), null);
                raidingGuild.sendBroadcast(Messages.get(Plugin.GUILDS).get("broadcasts.no longer raiding title", guild), null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerChunkUpdateIfRaidInterfering(PlayerChunkUpdateEvent event) {

        GuildChunk chunk = event.getNewGuildChunk();

        if (chunk == null) return;

        GPlayer player = event.getPlayer();
        Guild chunkHost = chunk.getGuild();

        if (chunkHost == null) return;

        // Chunk host is being raided and the player's guild is not raiding them
        if (chunkHost.getRaidedBy() != null && chunkHost.getRaidedBy() != event.getPlayer().getGuild()) {
            event.setCancelled(true);
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("raid interfering", chunkHost));
        }
    }
}
