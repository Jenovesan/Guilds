package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.event.GuildCreationEvent;
import com.guildwars.guildwars.guilds.event.PlayerGuildChangeEvent;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.files.PlayerData;

public class gCreate extends gCommand {

    @Override
    public String getDescription() {
        return Messages.getMsg("commands.create.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.create.usage");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        // Checks
        if (player.isInGuild()) {
            player.sendFailMsg(Messages.getMsg("commands.create.already in guild"));
            return;
        }

        String guildName = args[0];
        // Command return messages are handled in this method
        if (!gUtil.guildNameLegal(player, guildName)) {
            return;
        }

        // Create guild
        String guildDesc = args.length == 2 ? args[1] : "None";

        Guild newGuild = new Guild(player, guildName, guildDesc);

        // Add guild to guilds
        Guilds.get().add(newGuild);

        // Save data
        GuildData.get().save(newGuild);

        // Update gPlayer
        player.setGuild(newGuild);
        player.setGuildRank(GuildRank.LEADER);

        // Save data
        PlayerData.get().save(player);

        // Add to index
        GuildsIndex.get().add(newGuild);

        // Call Events
        GuildCreationEvent guildCreationEvent = new GuildCreationEvent(newGuild);
        guildCreationEvent.run();
        PlayerGuildChangeEvent playerGuildChangeEvent = new PlayerGuildChangeEvent(player, newGuild, PlayerGuildChangeEvent.Reason.CREATION);
        playerGuildChangeEvent.run();

        // Inform
        player.sendSuccessMsg(Messages.getMsg("commands.create.creation", newGuild));
    }
}
