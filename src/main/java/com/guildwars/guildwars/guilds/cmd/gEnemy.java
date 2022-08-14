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

        if (!gUtil.checkPermission(player, GuildPermission.RELATIONS, true)) {
            return;
        }

        Guild guildToEnemy;
        gPlayer possiblePlayerToEnemy = gPlayers.get(args[0]);
        if (possiblePlayerToEnemy != null) { // Player using player name to enemy guild
            if (!possiblePlayerToEnemy.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.player not in guild", possiblePlayerToEnemy));
                return;
            }
            guildToEnemy = possiblePlayerToEnemy.getGuild();
        } else { //Player using guild name to enemy guild
            guildToEnemy = Guilds.get(args[0]);
            if (guildToEnemy == null) {
                player.sendFailMsg(Messages.getMsg("commands.enemy.not a guild or player", args[0]));
                return;
            }
        }

        Guild playerGuild = player.getGuild();
        if (playerGuild.isEnemied(guildToEnemy)) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.already enemied", guildToEnemy));
            return;
        }

        if (playerGuild == guildToEnemy) {
            player.sendFailMsg(Messages.getMsg("commands.enemy.cannot enemy own guild"));
            return;
        }

        // Enemy guild
        playerGuild.enemy(guildToEnemy);

        // Have guildToEnemy enemy playerGuild
        guildToEnemy.enemy(playerGuild);

        // Inform playerGuild
        playerGuild.sendAnnouncement(Messages.getMsg("guild announcements.enemied guild", guildToEnemy));

        // Inform enemyGuild
        guildToEnemy.sendAnnouncement(Messages.getMsg("guild announcements.guild has enemied your guild", playerGuild));

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.enemy.successfully enemied", guildToEnemy));
    }
}
