package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.Indexing;
import com.guildwars.guildwars.guilds.event.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class IndexingEngine extends Engine {
    @EventHandler
    public void addPlayerOnLogin(GPlayerLoginEvent event) {
        GPlayer gPlayer = event.getGPlayer();

        Guild guild = gPlayer.getGuild();
        Player player = event.getPlayer();
        Indexing.get().add(player, guild);
    }

    @EventHandler
    public void removePlayerOnLogout(GPlayerQuitEvent event) {
        GPlayer gPlayer = event.getGPlayer();
        Guild guild = gPlayer.getGuild();
        Player player = event.getPlayer();
        Indexing.get().add(player, guild);
    }

    @EventHandler
    public void addGuildOnCreation(GuildCreatedEvent event) {
        Guild guild = event.getGuild();
        Indexing.get().add(guild);
    }

    @EventHandler
    public void updateGPlayerOnGuildChange(GPlayerGuildChangedEvent event) {
        GPlayer gPlayer = event.getGPlayer();
        Guild oldGuild = event.getOldGuild();
        Guild newGuild = event.getNewGuild();

        if (oldGuild != null) Indexing.get().removePlayerFromGuild(oldGuild, gPlayer);
        if (newGuild != null) Indexing.get().addPlayerToGuild(newGuild, gPlayer);
        Indexing.get().updatePlayerGuild(gPlayer.getPlayer(), newGuild);
    }

    @EventHandler
    public void updateGuildNameOnGuildRename(GuildRenameEvent event) {
        Guild guild = event.getGuild();
        String oldName = event.getOldName();
        String newName = event.getNewName();

        Indexing.get().updateName(guild, oldName, newName);
    }
}
