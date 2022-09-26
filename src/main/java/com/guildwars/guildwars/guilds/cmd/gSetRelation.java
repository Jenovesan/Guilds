package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.GuildPermission;
import com.guildwars.guildwars.guilds.Relation;
import com.guildwars.guildwars.guilds.cmd.arg.GuildArg;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;

public class gSetRelation extends gCommand {

    public gSetRelation() {
        // Name
        super("enemy", "truce");

        // Aliases
        addAlias("e");
        addAlias("t");
        addAlias("neutral");
        addAlias("n");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.RELATIONS));

        // Args
        addArg(new GuildArg(true));
    }

    @Override
    void perform() throws CmdException {
        // Args
        Relation relation = getRelation(getCmd());
        Guild targetGuild = readNextArg();

        // Prepare

        // Relation does not exist
        if (relation == null) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.setRelation.not a relation"));

        // Some relations are not requestable
        if (!relation.isRequestable()) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.setRelation.relation not requestable"));

        // Cannot set a relation with own guild
        if (targetGuild == guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.setRelation.own guild"));

        // Apply

        guild.setRelationWish(targetGuild, relation);

        // Inform

        Relation newRelation = guild.getRelationTo(targetGuild);

        // Relation was changed
        if (newRelation == relation) {
            // Inform guilds
            guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.new relation", Messages.get(Plugin.GUILDS).get("relations.setting." + newRelation.name()), guild.describe(targetGuild)));
            targetGuild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.new relation", Messages.get(Plugin.GUILDS).get("relations.setting." + newRelation.name()), targetGuild.describe(guild)));
        }
        // Relation was requested
        else {
            // Inform guilds
            guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.sent relation request", guild.describe(gPlayer), Messages.get(Plugin.GUILDS).get("relations.setting." + newRelation.name()), guild.describe(targetGuild)));
            targetGuild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.received relation request", targetGuild.describe(guild), Messages.get(Plugin.GUILDS).get("relations.setting." + newRelation.name())));
        }
    }

    private Relation getRelation(String from) {
        for (Relation relation : Relation.class.getEnumConstants()) {
            if (relation.name().equalsIgnoreCase(from)) return relation;
            for (String alias : relation.getAliases()) {
                if (alias.equalsIgnoreCase(from)) {
                    return relation;
                }
            }
        }
        return null;
    }
}
