package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.guilds.Guild;
import com.guildwars.guildwars.guilds.Guilds;
import com.guildwars.guildwars.guilds.event.GuildUnclaimEvent;
import com.guildwars.guildwars.guilds.files.Config;
import com.guildwars.guildwars.guilds.files.GuildData;
import com.guildwars.guildwars.guilds.files.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;

public class Home extends Engine{

    @EventHandler(priority = EventPriority.MONITOR)
    public void detectIfHomeIsBroken(BlockBreakEvent event) {
        testIfGuildBedIsDestroyed(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void detectIfHomeIsBlownUp(BlockExplodeEvent event) {
        testIfGuildBedIsDestroyed(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removeHomeIfUnclaimed(GuildUnclaimEvent event) {
        Guild guild = event.getGuild();

        if (!guild.hasHome()) return;

        Chunk eventChunk = event.getChunk().getChunk();

        Location guildHome = guild.getHome();

        // Guild home is in unclaimed chunk
        if (guildHome.getChunk() == eventChunk) {
            // Remove bed block.
            removeBedBlock(eventChunk.getWorld().getBlockAt(guildHome));

            // Remove guild home
            guild.setHome(null);

            // Save data
            GuildData.get().save(guild);

            // Send guild announcement
            guild.sendAnnouncement(Messages.getMsg("guild announcements.home unclaimed"));
        }
    }

    private void testIfGuildBedIsDestroyed(Block block) {
        System.out.println("triggerd");
        if (block.getType() != Material.RED_BED) return;

        // Get the bed pieces
        Location[] bedPieces = new Location[2];

        Bed bed = (Bed) block.getBlockData();

        bedPieces[0] = block.getLocation();
        if (bed.getPart() == Bed.Part.HEAD) {
            bedPieces[1] = block.getRelative(bed.getFacing().getOppositeFace()).getLocation();
        } else {
            bedPieces[1] = block.getRelative(bed.getFacing()).getLocation();
        }

        // Check if either piece of bed has been destroyed
        for (Guild guild : Guilds.get().getAll()) {
            Location guildHome = guild.getHome();
            for (Location bedLoc : bedPieces) {
                if (!bedLoc.equals(guildHome)) continue;

                // Guild home has been broken

                // Remove bed block.
                removeBedBlock(block);

                // Remove home
                guild.setHome(null);

                // Save data
                GuildData.get().save(guild);

                // Send guild announcement
                guild.sendAnnouncement(Messages.getMsg("guild announcements.home destroyed"));

                // Only one guild home can be destroyed, so if a guild home is destroyed, stop checking other guild homes
                return;
            }
        }
    }

    private void removeBedBlock(Block bedBlock) {
        // Get the other bed block
        Bed bed = (Bed) bedBlock.getBlockData();
        Block otherBedBlock = bed.getPart() == Bed.Part.HEAD ? bedBlock.getRelative(bed.getFacing().getOppositeFace()) : bedBlock.getRelative(bed.getFacing());

        // Set the bed blocks to air
        bedBlock.setType(Material.AIR, false);
        otherBedBlock.setType(Material.AIR);
    }
}
