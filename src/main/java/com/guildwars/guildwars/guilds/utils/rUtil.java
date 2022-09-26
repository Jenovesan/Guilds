package com.guildwars.guildwars.guilds.utils;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.GuildRank;
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
        Relation relation = from.getRelationTo(to);
        String msg = Messages.get(Plugin.GUILDS).get("relations.prefix." + relation.name());
        if (to instanceof Guild guild) {
            return msg + guild.getName();
        }
        else {
            Guild fromGuild = getGuild(from);
            GPlayer toGPlayer = ((GPlayer) to);
            // If the players are in the same guild, add the GuildRank prefix to the player being described
            if (fromGuild != null && toGPlayer.getGuild() == fromGuild) {
                GuildRank toGPlayerRank = toGPlayer.getGuildRank();
                String guildRankPrefix = Config.get(Plugin.GUILDS).getString("GuildRank prefixes." + toGPlayerRank.name());
                msg = msg.concat(guildRankPrefix);
            }
            return msg.concat(toGPlayer.getName());
        }
    }

    public static String describeBold(RelationParticipator from, RelationParticipator to) {
        return ChatColor.BOLD + describe(from, to);
    }

    private static Guild getGuild(RelationParticipator participator) {
        return participator instanceof Guild ? (Guild) participator : ((GPlayer) participator).getGuild();
    }
}
