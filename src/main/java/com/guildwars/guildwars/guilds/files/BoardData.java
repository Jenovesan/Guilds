package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.entity.GuildChunk;
import com.guildwars.guildwars.guilds.ObjectDataManager;

import java.io.File;
import java.util.HashMap;

public class BoardData extends ObjectDataManager<GuildChunk> {

    public static BoardData instance = new BoardData();
    public static BoardData get() {
        return instance;
    }

    public BoardData() {
        super("boarddata");
    }

    @Override
    public void save(GuildChunk chunk) {

        HashMap<String, Object> chunkData = new HashMap<>();

        String boardLocation = getBoardLocationString(chunk);

        chunkData.put("boardLocation", boardLocation);

        chunkData.put("guildId", chunk.getGuild().getId());

        saveRaw(boardLocation, chunkData);
    }

    private String getBoardLocationString(GuildChunk chunk) {
        return chunk.getBoardLocation().getX() + "," + chunk.getBoardLocation().getZ();
    }

    public void remove(GuildChunk chunk) {
        String fileName = getBoardLocationString(chunk);
        File rawFile = new File(getDataFolder() + "/" + fileName + ".yml");
        if (!rawFile.delete()) {
            System.out.println("Unable to delete " + fileName + ".yml");
        }
    }
}
