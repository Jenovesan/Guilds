package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.engine.AutoClaim;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class gAutoClaim extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.autoclaim.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.autoclaim.usage");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.CLAIM, false)) {
            return;
        }

        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.getMsg("autoclaim.cannot claim in world"));
            return;
        }

        // Add play to autoclaim set
        // Player is already auto-claiming.
        // Remove them from autoclaiming
        if (AutoClaim.isPlayer(player)) {
            AutoClaim.removePlayer(player);
        }
        // Player is not auto-claiming.
        // Add them to autoclaiming
        else {
            AutoClaim.addPlayer(player);
        }
        // Informing the player is handled in AutoClaim
    }
}
