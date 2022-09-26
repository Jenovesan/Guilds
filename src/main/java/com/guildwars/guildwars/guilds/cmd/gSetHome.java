package com.guildwars.guildwars.guilds.cmd;

import com.guildwars.guildwars.Messages;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.*;
import com.guildwars.guildwars.guilds.cmd.req.GuildPermissionReq;
import com.guildwars.guildwars.guilds.cmd.req.InGuildReq;
import com.guildwars.guildwars.guilds.files.GuildData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;

public class gSetHome extends gCommand{

    public gSetHome() {
        // Name
        super("sethome");

        // Reqs
        addReq(new InGuildReq());
        addReq(new GuildPermissionReq(GuildPermission.SETHOME));
    }

    @Override
    public void perform() throws CmdException {
        // Args
        Location playerLocation = player.getPlayer().getLocation();

        // Prepare

        // Can only set guild home in your territory
        if (Board.get().getGuildAt(playerLocation) != guild) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.sethome.not in guild territory"));

        // Apply

        // Create the bed
        BlockFace bedFace = getBedFace(playerLocation.getYaw());

        // Can only place the bed block if there are two airblocks for the bed to go
        // Ensures creating bed does not destroy any blocks
        if (playerLocation.getBlock().getRelative(bedFace).getType() != Material.AIR) throw new CmdException(Messages.get(Plugin.GUILDS).get("commands.sethome.cannot create bed here"));

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

        // Set guild home
        guild.setHome(playerLocation.getBlock().getLocation());

        // Inform

        // Send guild announcement
        guild.sendAnnouncement(Messages.get(Plugin.GUILDS).get("guild announcements.sethome", guild.describe(gPlayer)));

        // Inform player
        gPlayer.sendSuccessMsg(Messages.get(Plugin.GUILDS).get("commands.sethome.success"));
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
