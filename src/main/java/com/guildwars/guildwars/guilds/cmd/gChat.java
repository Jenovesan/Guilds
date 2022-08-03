package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.core.ChatChannel;
import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class gChat extends gCommand{

    @Override
    public String getDescription() {
        return Messages.getMsg("commands.chat.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.chat.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {

        // Checks
        if (!gPlayer.isInGuild()) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.chat.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(gPlayer, GuildPermission.CHAT)) {
            return;
        }

        Player player = gPlayer.getPlayer();
        ChatChannel playerChatChannel = ChatChannels.getPlayerChatChannel(player);

        // Join a chat channel
        if (args.length == 0) {
            // Join Guild chat
            if (playerChatChannel != ChatChannel.GUILD) {
                ChatChannels.setPlayerChatChannel(player, ChatChannel.GUILD);
                // Inform
                gPlayer.sendSuccessMsg(Messages.getMsg("commands.chat.joined guild chat"));
            }
            // Leave Guild chat
            else {
                ChatChannels.setPlayerChatChannel(player, null);
                // Inform
                gPlayer.sendSuccessMsg(Messages.getMsg("commands.chat.left guild chat"));
            }
        }
        // Send a message in guild chat
        else {
            String chatMsg = String.join(" ", args);
            ChatChannels.setPlayerChatChannel(player, ChatChannel.GUILD);
            player.chat(chatMsg);
            ChatChannels.setPlayerChatChannel(player, playerChatChannel);
        }

    }
}
