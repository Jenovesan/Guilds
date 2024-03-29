package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.BoardMap;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class MapAuto extends Engine {

    @EventHandler(priority = EventPriority.MONITOR)
    public void sendMapOnPlayerChunkUpdate(PlayerChunkUpdateEvent event) {
        if (event.isCancelled()) return;

        GPlayer player = event.getPlayer();

        if (!player.isAutoMapping()) return;

        BoardMap map = new BoardMap(player);
        map.sendMap();
    }
}
