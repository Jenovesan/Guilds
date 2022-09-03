package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.core.ChatChannel;
import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.entity.Player;

public class gChat extends gCommand {

    public gChat() {
        super("chat");
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {

        // Checks
        if (!gPlayer.isInGuild()) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(gPlayer, GuildPermission.CHAT, true)) {
            return;
        }

        Player player = gPlayer.getPlayer();
        ChatChannel playerChatChannel = ChatChannels.getPlayerChatChannel(player);

        // Join a chat channel
        if (args.length == 0) {
            // Join Guild chat
            if (playerChatChannel != ChatChannel.GUILD) {
                joinGuildChat(gPlayer, player);
            }
            // Leave Guild chat
            else {
                leaveGuildChat(gPlayer, player);
            }
        }
        else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("g")) {
            joinGuildChat(gPlayer, player);
        }
        else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("p")) {
            leaveGuildChat(gPlayer, player);
        }
        // Send a message in guild chat
        else {
            String chatMsg = String.join(" ", args);
            ChatChannels.setPlayerChatChannel(player, ChatChannel.GUILD);
            player.chat(chatMsg);
            ChatChannels.setPlayerChatChannel(player, playerChatChannel);
        }
    }

    private void joinGuildChat(gPlayer gPlayer, Player player) {
        ChatChannels.setPlayerChatChannel(player, ChatChannel.GUILD);
        // Inform
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.chat.joined guild chat"));
    }

    private void leaveGuildChat(gPlayer gPlayer, Player player) {
        ChatChannels.setPlayerChatChannel(player, null);
        // Inform
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.chat.left guild chat"));
    }
}
