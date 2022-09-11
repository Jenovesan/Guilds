package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.GuildWars;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.Location;

public class gHome extends gCommand {

    public gHome() {
        super("home");
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.HOME, true)) return;

        Guild guild = player.getGuild();

        if (!guild.hasHome()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.home.no home"));
            return;
        }

        Location guildHome = guild.getHome();

        player.teleport(Config.get(Plugin.GUILDS).getInt("teleport to home charge up (ticks)"), guildHome);
    }
}
