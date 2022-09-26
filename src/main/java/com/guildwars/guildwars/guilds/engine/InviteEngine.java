package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.entity.GPlayer;
import com.guildwars.guildwars.entity.Guild;
import com.guildwars.guildwars.entity.Invitation;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.Indexing;

public class InviteEngine extends Engine {

    public InviteEngine() {
        super(Config.get(Plugin.GUILDS).getLong("invite expiry update (ticks)"));
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Guild guild : Guilds.get().getAll()) {
            for (Invitation invite : guild.getInvites()) {
                // Invite has expired
                if (currentTime > invite.getExpireTime()) {
                    // Apply
                    guild.removeInvite(invite);

                    // Inform
                    GPlayer gPlayer = Indexing.get().getGPlayerByUUID(invite.getPlayerUUID());
                    gPlayer.sendNotifyMsg(Messages.get(Plugin.GUILDS).get("invite.expired", gPlayer.describe(guild)));
                }
            }
        }
    }
}
