package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gPlayer;
import com.guildwars.guildwars.guilds.gUtil;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class gKick extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.kick.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.kick.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer kicker, String[] args) {

        // Checks
        if (!kicker.isInGuild()) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.not in guild"));
            return;
        }

        Guild guild = kicker.getGuild();

        OfflinePlayer kickee = guild.getOfflinePlayer(args[0]);

        if (kickee == null) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.kickee not found").replace("<input>", args[0]));
            return;
        }

        if (!gUtil.checkPermission(kicker, GuildPermission.INVITE)) {
            return;
        }

        if (!guild.hasHigherRank(kicker.getUUID(), kickee.getUniqueId())) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.guild rank not higher"));
            return;
        }

        // Kick player
        guild.kickPlayer(kicker.getName(), kickee);

        // Call event
        Player kickeePlayer = kickee.getPlayer();
        if (kickeePlayer != null) {
            // Call event
            Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(kickeePlayer, null, PlayerGuildChangeEvent.Reason.KICKED));
            // Inform kickee
            pUtil.sendNotifyMsg(kickeePlayer, Messages.getMsg("commands.kick.kickee kicked msg").replace("<name>", guild.getName()));
        }

        // Inform kicker
        kicker.sendSuccessMsg(Messages.getMsg("commands.kick.successfully kicked").replace("<name>", Objects.requireNonNullElse(kickee.getName(), "")));
    }
}
