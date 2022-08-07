package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.entity.Player;

public class gClaim extends gCommand{
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer gPlayer, String[] args) {

        if (!gPlayer.isInGuild()) {
            gPlayer.sendFailMsg(Messages.getMsg("commands.not in guild", gPlayer.getPlayer(), null, args, null, null, null, null));
            return;
        }

        if (!gUtil.checkPermission(gPlayer, GuildPermission.CLAIM)) {
            return;
        }

        Player player = gPlayer.getPlayer();
        int chunkX = player.getLocation().getChunk().getX();
        int chunkZ = player.getLocation().getChunk().getZ();


    }
}
