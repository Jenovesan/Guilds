package com.guildwars.guildwars.guilds.engine;

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

public class Raiding extends Engine {

    public Raiding() {
        super(Config.get().getLong("raiding kick non-raiders update (tick)"));
    }

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


            // Broadcasts
            playerGuild.sendBroadcast(Messages.getMsg("broadcasts.raidable title"), Messages.getMsg("broadcasts.raidable subtitle", raidDuration));
            killerGuild.sendBroadcast(Messages.getMsg("broadcasts.raiding title", playerGuild), Messages.getMsg("broadcasts.raiding subtitle", raidDuration));
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
}
