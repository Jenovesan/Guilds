package com.guildwars.guildwars.guilds.event;

import com.guildwars.guildwars.entity.Guild;

public class GuildRenameEvent extends GuildsEvent {
    private final Guild guild;
    private final String oldName;
    private final String newName;

    public Guild getGuild() {
        return guild;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public GuildRenameEvent(Guild guild, String oldName, String newName) {
        this.guild = guild;
        this.oldName = oldName;
        this.newName = newName;
    }
}
