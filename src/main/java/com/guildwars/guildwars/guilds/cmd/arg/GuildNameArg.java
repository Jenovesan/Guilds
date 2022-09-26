package com.guildwars.guildwars.guilds.cmd.arg;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.cmd.CmdException;
import com.guildwars.guildwars.utils.util;

import java.util.List;

public class GuildNameArg extends Arg<String>{

    public GuildNameArg(boolean required) {
        super(required, "name");
    }

    @Override
    public String read(String arg, GPlayer gPlayer) throws CmdException {
        // Check if guild name is too long
        int guildNameCharacterLimit = Config.get(Plugin.GUILDS).getInt("max characters in guild name");
        if (arg.toCharArray().length > guildNameCharacterLimit) throw new CmdException(Messages.get(Plugin.GUILDS).get("guild naming.name too long", arg));

        // Check if the guild name exists already
        for (Guild guild : Guilds.get().getAll()) {
            if (guild.getName().equalsIgnoreCase(arg)) throw new CmdException(Messages.get(Plugin.GUILDS).get("guild naming.name exists", arg));
        }

        // Check if guild name contains any non-listed characters
        List<Character> legalGuildNameCharacters = Config.get(Plugin.GUILDS).getCharacterList("valid guild name characters");
        for (Character character : arg.toCharArray()) {
            if (!legalGuildNameCharacters.contains(character)) throw new CmdException(Messages.get(Plugin.GUILDS).get("guild naming.name contains not legal character", character));
        }

        // Check if the name is a blacklisted guild name
        List<String> blackListedGuildNames = Config.get(Plugin.GUILDS).getStringList("blacklisted guild names");
        if (util.containsIgnoreCase(blackListedGuildNames, arg)) throw new CmdException(Messages.get(Plugin.GUILDS).get("guild naming.name blacklisted"));

        // Guild name passes all checks.
        // Guild name is deemed legal.
        return arg;
    }
}
