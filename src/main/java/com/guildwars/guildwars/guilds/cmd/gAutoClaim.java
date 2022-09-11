package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;

public class gAutoClaim extends gCommand {

    public gAutoClaim() {
        super("autoclaim");
        mustBeInGuild(true);
        setMinPermission(GuildPermission.CLAIM);
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!gUtil.isInMainWorld(player)) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("autoclaim.cannot claim in world"));
            return;
        }

        // Add play to autoclaim set
        // Player is already auto-claiming.
        // Remove them from autoclaiming
        if (player.isAutoClaiming()) {
            player.setAutoClaiming(false);
            player.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("autoclaiming.disabled"));
        }
        // Player is not auto-claiming.
        // Add them to autoclaiming
        else {
            player.setAutoClaiming(true);
            player.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("autoclaiming.enabled"));
        }
        // Informing the player is handled in AutoClaim
    }
}
