package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.Indexing;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.guilds.entity.GPlayer;

public class GuildArg extends Arg<Guild> {

    public GuildArg(boolean required) {
        super(required, "guild");
    }

    @Override
    public Guild read(String arg, GPlayer gPlayer) throws CmdException {

        // Try to get guild by player
        GPlayer targetPlayer = Indexing.get().getGPlayerByName(arg);

        // Sender is trying to get a guild by a members' name
        if (targetPlayer != null) {
            // targetPlayer is not in a guild
            if (!targetPlayer.isInGuild()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.player not in guild", gPlayer.describe(targetPlayer)));

            return targetPlayer.getGuild();
        }
        // Try and get guild by guild name
        else {
            Guild targetGuild = Indexing.get().getGuildByName(arg);
            if (targetGuild == null) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.not a guild or player", arg));
            return targetGuild;
        }
    }
}
