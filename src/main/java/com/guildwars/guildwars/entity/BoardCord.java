package com.guildwars.guildwars.entity;

import com.guildwars.guildwars.Config;
import com.guildwars.guildwars.Plugin;
import com.guildwars.guildwars.guilds.Board;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class BoardCord {

    private final int xCord;
    private final int zCord;

    public int getX() {
        return xCord;
    }

    public int getZ() {
        return zCord;
    }

    public BoardCord(Chunk chunk) {
        xCord = getCord(chunk.getX());
        zCord = getCord(chunk.getZ());
    }

    public BoardCord(Location location) {
        xCord = getCord(location.getChunk().getX());
        zCord = getCord(location.getChunk().getZ());
    }

    public BoardCord(int xCord, int zCord, boolean calculate) {
        this.xCord = calculate ? getCord(xCord) : xCord;
        this.zCord = calculate ? getCord(zCord) : zCord;
    }

    public boolean isOutOfBounds() {
        return xCord < 0 || zCord < 0 || xCord > (Board.get().getRadius() * 2) - 1 || zCord > (Board.get().getRadius() * 2) - 1;
    }

    private int getCord(int chunkCord) {
        // Is a positive cord
        if (chunkCord > 0) {
            return Board.get().getRadius() + chunkCord;
        }
        // Is a negative cord
        else {
            return Board.get().getRadius() - (chunkCord * -1);
        }
    }

    public Chunk getBukkitChunk() {
        int reversedXCord = getReversedCord(xCord);
        int reversedZCord = getReversedCord(zCord);

        return Bukkit.getWorld(Config.get(Plugin.GUILDS).getString("world name")).getChunkAt(reversedXCord, reversedZCord);
    }

    private int getReversedCord(int cord) {
        if (cord > Board.get().getRadius()) {
            return cord - Board.get().getRadius();
        }
        // Chunk cord is negative
        else {
            return (Board.get().getRadius() - cord) * -1;
        }
    }
}
