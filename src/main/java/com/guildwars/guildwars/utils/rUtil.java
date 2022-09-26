package com.guildwars.guildwars.utils;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.entity.Guild;
import com.guildwars.guildwars.guilds.Relation;
import com.guildwars.guildwars.guilds.RelationParticipator;
import com.guildwars.guildwars.guilds.entity.GPlayer;
import org.bukkit.ChatColor;

public class rUtil {
    public static Relation getRelation(RelationParticipator from, RelationParticipator to) {
        Guild fromGuild = getGuild(from);
        Guild toGuild = getGuild(to);

        if (fromGuild == null || toGuild == null) {
            return Relation.NEUTRAL;
        }
        else if (fromGuild == toGuild) {
            return Relation.OWN;
        }
        else {
            Relation fromGuildRelationWish = fromGuild.getRelationWishWith(toGuild);
            Relation toGuildRelationWish = toGuild.getRelationWishWith(fromGuild);
            return toGuildRelationWish.getValue() > fromGuildRelationWish.getValue() ? toGuildRelationWish : fromGuildRelationWish;
        }
    }

    public static String describe(RelationParticipator from, RelationParticipator to) {
        String name = to instanceof Guild ? ((Guild) to).getName() : ((GPlayer) to).getName();
        Relation relation = from.getRelationTo(to);
        return Messages.get(Plugin.GUILDS).get("relations.prefix." + relation.name()) + name;
    }

    public static String describeBold(RelationParticipator from, RelationParticipator to) {
        return ChatColor.BOLD + describe(from, to);
    }

    private static Guild getGuild(RelationParticipator participator) {
        return participator instanceof Guild ? (Guild) participator : ((GPlayer) participator).getGuild();
    }
}
