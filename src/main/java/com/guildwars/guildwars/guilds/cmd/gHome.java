package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.cmd.req.GuildHasHome;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import org.bukkit.Location;

public class gHome extends gCommand {

    public gHome() {
        // Name
        super("home");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.HOME));
        addReq(new GuildHasHome());
    }

    @Override
    public void perform() {
        // Prepare
        Location guildHome = guild.getHome();

        // Apply
        gPlayer.teleport(Config.get(Plugin.GUILDS).getInt("teleport to home charge up (ticks)"), guildHome);
    }
}
