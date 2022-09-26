package com.guildwars.guildwars.core;

import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GPlayerGuildChangedEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatChannels implements Listener {

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatChannel playerChatChannel = getPlayersChatChannel().get(player);

        // If player is not in a chat channel, do not edit the message's recipients
        if (playerChatChannel == null) {
            return;
        }

        // Replace the message's recipients
        event.getRecipients().clear();

        Set<Player> channelRecipients = ChatChannels.getChannelRecipients(player, playerChatChannel);
        event.getRecipients().addAll(channelRecipients);

        // Set message format
        event.setFormat(getChannelFormat(player, event.getMessage(), playerChatChannel));
    }

    @EventHandler
    public void removePlayerChannelDataOnLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getPlayersChatChannel().remove(player);
    }


    private static HashMap<Player, ChatChannel> playersChatChannel = new HashMap<>();

    public static HashMap<Player, ChatChannel> getPlayersChatChannel() {
        return playersChatChannel;
    }

    public static ChatChannel getPlayerChatChannel(Player player) {
        return getPlayersChatChannel().get(player);
    }

    public static void setPlayerChatChannel(Player player, ChatChannel channel) {
        if (channel != null) {
            getPlayersChatChannel().put(player, channel);
        }
        // Remove player from data if they are not in a channel
        else {
            getPlayersChatChannel().remove(player);
        }
    }

    public static Set<Player> getChannelRecipients(Player sender, ChatChannel channel) {
        HashSet<Player> recipients = new HashSet<>();
        switch (channel) {
            case GUILD:
                Guild senderGuild = Indexing.get().getGuildByPlayer(sender);
                for (GPlayer player : senderGuild.getOnlinePlayers()) {
                    if (gUtil.checkPermission(player, GuildPermission.CHAT)) {
                        recipients.add(player.getPlayer());
                    }
                }
        }
        return recipients;
    }

    @EventHandler
    public void updateChannelOnGuildChange(GPlayerGuildChangedEvent event) {
        Player player = event.getGPlayer().getPlayer();

        // Player was in guild chat
        if (getPlayersChatChannel().get(player) == ChatChannel.GUILD) {
            // Remove player from guild's chat channel when they leave the guild
            setPlayerChatChannel(player, null);
        }
        // Keep them in the same channel.
        // Channel not affected by them leaving their guild.
    }

    private static String getChannelFormat(Player player, String msg, ChatChannel channel) {
        String rawChannelFormat = ChatChannel.getRawChannelFormat(channel);
        return ChatColor.translateAlternateColorCodes('&',
                rawChannelFormat.replace("<name>", player.getName()).replace("<msg>", msg).replace("<display name>", player.getDisplayName()));
    }
}
