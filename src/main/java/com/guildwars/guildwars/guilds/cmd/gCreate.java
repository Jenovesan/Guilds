package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Bukkit;

public class gCreate extends gCommand {

    @Override
    public String getDescription() {
        return Messages.getMsg("commands.create.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.create.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {

        // Checks
        if (gPlayer.isInGuild()) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.create.already in guild", gPlayer.getPlayer(), null, args, gPlayer.getGuild(), null, gPlayer.getGuildRank(), GuildRank.LEADER));
            return;
        }

        String guildName = args[0];
        // Command return messages are handled in this method
        if (!gUtil.guildNameLegal(gPlayer.getPlayer(), guildName)) {
            return;
        }

        // Create guild
        String guildDesc = args.length == 2 ? args[1] : "None";

        Guild newGuild = new Guild(gPlayer.getUUID(), guildName, guildDesc);
        Guilds.saveGuildData(newGuild);

        // Update gPlayer
        gPlayer.joinedNewGuild(newGuild);

        // Call Event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(gPlayer.getPlayer(), newGuild, PlayerGuildChangeEvent.Reason.CREATION));

        // Inform
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.create.creation", gPlayer.getPlayer(), null, args, gPlayer.getGuild(), gPlayer.getGuild(), gPlayer.getGuildRank(), GuildRank.LEADER));
    }
}
