package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.files.Messages;
import com.guildwars.guildwars.guilds.gUtil;
import org.bukkit.entity.Player;

public class gWho extends gCommand{
    @Override
    public String getDescription() {
        return Messages.getMsg("commands.who.description");
    }

    @Override
    public String getUsage() {
        return Messages.getMsg("commands.who.usage");
    }

    @Override
    public void perform(Player player, String[] args) {
        Guild guild = gUtil.getPlayerGuild(player.getUniqueId());
        if (guild != null) {
            player.sendMessage(guild.name + guild.description);
        } else {
            player.sendMessage("Player is not in a guild");
        }
    }
}
