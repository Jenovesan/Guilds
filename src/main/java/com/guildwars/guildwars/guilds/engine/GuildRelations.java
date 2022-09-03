package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.event.GuildDisbandEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.event.EventHandler;

public class GuildRelations extends Engine {
    @EventHandler
    public void removeDataFromOtherGuildsOnDisband(GuildDisbandEvent event) {
        Guild disbandedGuild = event.getGuild();
        for (Guild guild : Guilds.get().getAll()) {
            // Remove from enemies
            guild.getEnemies().remove(disbandedGuild);
            // Remove from raidedBy
            if (guild.getRaidedBy() == disbandedGuild) {
                guild.setRaidedBy(null);
                // Save data
                GuildData.get().save(guild);
            }
        }
    }
}
