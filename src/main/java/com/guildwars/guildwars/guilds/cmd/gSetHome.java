package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
public class gSetHome extends gCommand{

    public gSetHome() {
        super("sethome");
        mustBeInGuild(true);
        setMinPermission(GuildPermission.SETHOME);
    }

    @Override
    public void perform(gPlayer player, String[] args) {

        Location playerLocation = player.getPlayer().getLocation();

        if (Board.getGuildAt(playerLocation) != player.getGuild()) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.sethome.can only sethome in territory"));
            return;
        }

        // Create the bed
        BlockFace bedFace = getBedFace(playerLocation.getYaw());
        if (playerLocation.getBlock().getRelative(bedFace).getType() != Material.AIR) {
            player.sendFailMsg(Messages.get(Plugin.GUILDS).get("commands.sethome.cannot create bed here"));
            return;
        }
        // Set the bed head
        playerLocation.getBlock().getRelative(bedFace).setType(Material.RED_BED);
        Bed bedHead = (Bed) playerLocation.getBlock().getRelative(bedFace).getState().getBlockData();
        bedHead.setPart(Bed.Part.HEAD);
        bedHead.setFacing(bedFace);
        playerLocation.getBlock().getRelative(bedFace).setBlockData(bedHead);
        // Set bed foot
        playerLocation.getBlock().setType(Material.RED_BED);
        Bed bedFoot = (Bed) playerLocation.getBlock().getState().getBlockData();
        bedFoot.setPart(Bed.Part.FOOT);
        bedFoot.setFacing(bedFace);
        playerLocation.getBlock().setBlockData(bedFoot);

        Guild guild = player.getGuild();

        // Set guild home
        guild.setHome(playerLocation.getBlock().getLocation());

        // Save data
        GuildData.get().save(guild);

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.sethome", player));

        // Inform player
        player.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.sethome.successfully sethome"));
    }

    private BlockFace getBedFace(float yaw) {
        // Get direction
        int dir = (int) (yaw % 360);
        if (dir < 0) {
            dir += 360.0;
        }

        // Get cardinal direction
        if (dir > 315 || dir < 45) {
            return BlockFace.SOUTH;
        } else if (dir > 45 && dir < 135) {
            return BlockFace.WEST;
        } else if (dir > 135 && dir < 225) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }
}
