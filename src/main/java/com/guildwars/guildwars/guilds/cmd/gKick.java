package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.utils.pUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
            kicker.sendFailMsg(Messages.getMsg("commands.invite.invitee not found", kicker.getPlayer(), null, args, kicker.getGuild(), null, kicker.getGuildRank(), null));
            return;
        }

        Guild guild = kicker.getGuild();

        OfflinePlayer kickee = guild.getOfflinePlayer(args[0]);

        if (kickee == null) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.kickee not found", kicker.getPlayer(), null, args, guild, null, kicker.getGuildRank(), null));
            return;
        }

        if (!gUtil.checkPermission(kicker, GuildPermission.INVITE)) {
            return;
        }

        GuildRank kickerRank = guild.getRank(kicker.getUUID());
        GuildRank kickeeRank = guild.getRank(kickee.getUniqueId());
        if (kickerRank.level <= kickeeRank.level) {
            kicker.sendFailMsg(Messages.getMsg("commands.kick.guild rank not higher", kicker.getPlayer(), kickee.getPlayer(), args, guild, null, kickerRank, kickeeRank));
            return;
        }

        // Kick player
        guild.kickPlayer(kicker.getPlayer(), kickee);

        // Call event
        Player kickeePlayer = kickee.getPlayer();
        if (kickeePlayer != null) {
            // Call event
            Bukkit.getServer().getPluginManager().callEvent(new PlayerGuildChangeEvent(kickeePlayer, null, PlayerGuildChangeEvent.Reason.KICKED));
            // Inform kickee
            pUtil.sendNotifyMsg(kickeePlayer, Messages.getMsg("commands.kick.kickee kicked msg", kickeePlayer, kicker.getPlayer(), args, guild, null, kickeeRank, kickerRank));
        }

        // Inform kicker
        kicker.sendSuccessMsg(Messages.getMsg("commands.kick.successfully kicked", kicker.getPlayer(), kickeePlayer, args, guild, null, kickerRank, kickeeRank));
    }
}
