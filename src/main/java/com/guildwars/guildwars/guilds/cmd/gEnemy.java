package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class gEnemy extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.enemy.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.enemy.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (!player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.not in guild", player, null, String.join(" ", args)));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.RELATIONS)) {
            return;
        }

        Guild guildToEnemy;
        gPlayer possiblePlayerToEnemy = gPlayers.get(args[0]);
        if (possiblePlayerToEnemy != null) { // Player using player name to enemy guild
            if (!possiblePlayerToEnemy.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.player not in guild", player, possiblePlayerToEnemy, String.join(" ", args)));
                return;
            }
            guildToEnemy = possiblePlayerToEnemy.getGuild();
        } else { //Player using guild name to enemy guild
            guildToEnemy = Guilds.get(args[0]);
            if (guildToEnemy == null) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.not a guild or player", player, null, String.join(" ", args)));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (playerGuild.isEnemied(guildToEnemy)) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.already enemied", player, possiblePlayerToEnemy, String.join(" ", args)));
            return;
        }

        if (playerGuild == guildToEnemy) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.cannot enemy own guild", player, possiblePlayerToEnemy, String.join(" ", args)));
            return;
        }

        // Enemy guild
        playerGuild.enemy(player, guildToEnemy);

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.enemy.successfully enemied", player, possiblePlayerToEnemy, String.join(" ", args)));
    }
}
