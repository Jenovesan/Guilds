package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.guilds.files.messages;
import org.bukkit.entity.Player;

public class gCreate extends gCommand {

    @Override
    public String getDescription() {
        return messages.get().getString("commands.create.description");
    }

    @Override
    public String getUsage() {
        return messages.get().getString("commands.create.usage");
    }

    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage("worked");
    }
}
