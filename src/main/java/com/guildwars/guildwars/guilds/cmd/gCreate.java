package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.GuildsManager;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        Guild guild = new Guild(player, args[0], args[1]);
        GuildsManager.saveGuild(guild);
        GuildsManager.getGuilds().add(guild);
    }
}
