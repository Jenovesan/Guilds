package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;

public class gJoin extends gCommand{

    public gJoin() {
        super("join");
        this.setMinArgs(1);
    }

    @Override
    public void perform(gPlayer player, String[] args) {
        // Checks
        if (player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.join.in guild"));
            return;
        }

        Guild guildToJoin;
        gPlayer possiblePlayerToJoin = gPlayersIndex.get().getByName(args[0]);
        if (possiblePlayerToJoin != null) { // Player using player name to join guild
            if (!possiblePlayerToJoin.isInGuild()) {
                player.sendFailMsg(Messages.getMsg("commands.join.player not in guild", possiblePlayerToJoin));
                return;
            }
            guildToJoin = possiblePlayerToJoin.getGuild();
        } else { //Player using guild name to join guild
            guildToJoin = GuildsIndex.get().getByName(args[0]);
            if (guildToJoin == null) {
                player.sendFailMsg(Messages.getMsg("commands.not a guild or player", args[0]));
                return;
            }
        }

        if (!guildToJoin.isInvited(player)) {
            player.sendFailMsg(Messages.getMsg("commands.join.not invited", guildToJoin));
            return;
        }

        if (guildToJoin.isFull()) {
            player.sendFailMsg(Messages.getMsg("commands.join.guild is full", guildToJoin));
            // Send guild announcement saying player tried to join, but guild was full
            guildToJoin.sendAnnouncement(Messages.getMsg("guilds announcements.guild was full", player));
            return;
        }

        // Join guild

        // Send Guild Announcement
        // This is done first because if it was done after the player was added to the guild, the new player would receive this announcement
        guildToJoin.sendAnnouncement(Messages.getMsg("guild announcements.player join", player));

        guildToJoin.addPlayer(player);

        // Save data
        GuildData.get().save(guildToJoin);

        // Update gPlayer
        player.joinedNewGuild(guildToJoin);

        // Call event
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(player, guildToJoin, PlayerGuildChangeEvent.Reason.JOIN);
        playerGuildChangeEvent.run();

        // Remove invite
        guildToJoin.removeInvite(player);

        // Inform player
        player.sendSuccessMsg(Messages.getMsg("commands.join.successfully joined", guildToJoin));
    }
}
