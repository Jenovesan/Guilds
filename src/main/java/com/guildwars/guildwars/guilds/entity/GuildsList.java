package com.guildwars.guildwars.guilds.entity;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Guilds;

import java.util.ArrayList;
import java.util.List;

public class GuildsList {

    public final GPlayer gPlayer;
    public List<String> list = new ArrayList<>();
    private final int guildsPerPage = Config.get(Plugin.GUILDS).getInt("guilds per page in gList");
    public final int pages;

    public GuildsList(GPlayer gPlayer) {
        this.gPlayer = gPlayer;
        loadList();
        pages = (int) Math.ceil((double) list.size() / guildsPerPage);
    }

    private void loadList() {
        for (Guild guild : getOnlineGuilds()) {
            int onlinePlayers = guild.getNumberOfOnlinePlayers();
            int playerCount = guild.getPlayerCount();
            int claims = guild.getNumberOfClaims();
            int power = guild.getPower();
            int maxPower = guild.getMaxPower();
            list.add(Messages.get(Plugin.GUILDS).get("commands.list.list construction.data", gPlayer.describe(guild), onlinePlayers, playerCount, claims, power, maxPower));
        }
    }

    private List<Guild> getOnlineGuilds() {
        return Guilds.get().getAll().stream().filter(Guild::hasOnlinePlayer).toList();
    }

    public boolean isPage(int page) {
        return page >= 1 && page <= pages;
    }

    public String getPage(int page) {
        String msg = Messages.get(Plugin.GUILDS).get("commands.list.list construction.header") + "\n";
        for (int i = (page - 1) * guildsPerPage; i < Math.min(page * guildsPerPage, list.size()); i++) {
            msg = msg.concat(list.get(i) + "\n");
        }
        msg = msg.concat(Messages.get(Plugin.GUILDS).get("commands.list.list construction.footer", page, pages));
        return msg;
    }

    private String getSecurityColor(Guild guild) {

        if (guild.isGettingRaided()) {
            return "";
        }

        int power = guild.getPower();
        int onlinePlayers = guild.getNumberOfOnlinePlayers();
        int claims = guild.getNumberOfClaims();
        int powerLossOnDeath = Config.get(Plugin.GUILDS).getInt("power change on death");

        // Safety will be greater than 0 if... if all players in the guild died once, they will still have enough power to not be pillageable
        // Safety will be less than 0 if... if all players in the guild died once, they will become pillageable
        int safety = claims - (power + (onlinePlayers * powerLossOnDeath));
        return "";
    }
}
