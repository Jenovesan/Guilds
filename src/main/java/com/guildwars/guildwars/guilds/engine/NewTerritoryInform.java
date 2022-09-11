package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildChunk;
import com.guildwars.guildwars.guilds.event.PlayerChunkUpdateEvent;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class NewTerritoryInform extends Engine {

    private static final int FADE_IN = Config.get(Plugin.GUILDS).getInt("broadcasts.new territory.fadeIn");
    private static final int STAY = Config.get(Plugin.GUILDS).getInt("broadcasts.new territory.stay");
    private static final int FADE_OUT = Config.get(Plugin.GUILDS).getInt("broadcasts.new territory.fadeOut");


    @EventHandler
    public void sendTitleOnEnterNewTerritory(PlayerChunkUpdateEvent event) {
        Player playerPlayer = event.getPlayer().getPlayer();
        GuildChunk oldChunk = event.getOldGuildChunk();
        GuildChunk newChunk = event.getNewGuildChunk();

        if (newChunk == null) {
            // Player moved into outlands
            if (oldChunk != null) {
                playerPlayer.sendTitle(
                        Messages.get(Plugin.GUILDS).get("new territory titles.outlands.title"),
                        Messages.get(Plugin.GUILDS).get("new territory titles.outlands.subtitle"),
                        FADE_IN, STAY, FADE_OUT
                );
            }
            return;
        }
        // Player is in map
        Guild oldChunkGuild = oldChunk != null ? oldChunk.getGuild() : null;
        Guild newChunkGuild = newChunk.getGuild();

        if (oldChunk == null || oldChunkGuild != newChunkGuild) {
            // Player moved into wilderness
            if (newChunk.isWilderness()) {
                playerPlayer.sendTitle(
                        Messages.get(Plugin.GUILDS).get("new territory titles.wilderness.title"),
                        Messages.get(Plugin.GUILDS).get("new territory titles.wilderness.subtitle"),
                        FADE_IN, STAY, FADE_OUT
                );
            }
            // Player moved into other guild's territory
            else {
                gPlayer player = event.getPlayer();
                playerPlayer.sendTitle(
                        Messages.get(Plugin.GUILDS).get("new territory titles.guild.title", gUtil.getGuildToName(player, newChunkGuild)),
                        Messages.get(Plugin.GUILDS).get("new territory titles.guild.subtitle", gUtil.getGuildToDesc(player, newChunkGuild)),
                        FADE_IN, STAY, FADE_OUT
                );
            }
        }
    }
}
