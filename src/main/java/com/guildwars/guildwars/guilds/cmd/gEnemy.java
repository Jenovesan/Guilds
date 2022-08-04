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
            player.sendFailMsg(Messages.getMsg("commands.not in guild"));
            return;
        }

        if (!gUtil.checkPermission(player, GuildPermission.RELATIONS)) {
            return;
        }

        Guild guildToEnemy;
        Player possiblePlayerToEnemy = Bukkit.getPlayerExact(args[0]);
        if (possiblePlayerToEnemy != null) { // Player using player name to enemy guild
            gPlayer gPlayerToEnemy = gPlayers.get(possiblePlayerToEnemy);
            if (!gPlayerToEnemy.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.player not in guild").replace("<name>", possiblePlayerToEnemy.getName()));
                return;
            }
            guildToEnemy = gPlayerToEnemy.getGuild();
        } else { //Player using guild name to enemy guild
            guildToEnemy = Guilds.get(args[0]);
            if (guildToEnemy == null) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.not a guild or player").replace("<input>", args[0]));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (playerGuild.isEnemied(guildToEnemy)) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.already enemied").replace("<name>", guildToEnemy.getName()));
            return;
        }

        if (playerGuild == guildToEnemy) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.cannot enemy own guild"));
            return;
        }

        // Enemy guild
        playerGuild.enemy(player.getName(), guildToEnemy);

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.enemy.successfully enemied").replace("<name>", guildToEnemy.getName()));
    }
}
