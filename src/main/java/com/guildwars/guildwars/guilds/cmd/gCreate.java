package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.utils.util;
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
            gPlayer.sendFailMsg(Messages.getMsg("commands.create.already in guild"));
            return;
        }

        String guildName = args[0];
        if (guildName.toCharArray().length > Config.get().getInt("max characters in guild name")) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.create.guild name too long"));
            return;
        }
        if (util.containsIgnoreCase(Guilds.getAllGuildNames(), guildName)) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.create.guild name exists"));
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
        gPlayer.sendSuccessMsg(Messages.getMsg("commands.create.creation"));
    }
}
