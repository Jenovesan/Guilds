package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.core.ChatChannel;
import com.guildwars.guildwars.core.ChatChannels;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.cmd.arg.BoolArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.utils.util;

public class gChat extends gCommand {

    public gChat() {
        // Name
        super("chat");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.CHAT));

        // Args
        addArg(new BoolArg(false));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        Boolean enable = readNextArg();

        // Prepare

        ChatChannel newChatChannel;
        if (enable != null) newChatChannel = enable ? ChatChannel.GUILD : ChatChannel.PUBLIC;
        else newChatChannel = ChatChannels.getPlayerChatChannel(player) != ChatChannel.GUILD ? ChatChannel.GUILD : ChatChannel.PUBLIC;

        // Apply

        ChatChannels.setPlayerChatChannel(player, newChatChannel);

        // Inform

        gPlayer.sendNotifyMsg(Messages.get(Plugin.CORE).get("chat channels.joined", util.formatEnum(newChatChannel)));
    }
}
