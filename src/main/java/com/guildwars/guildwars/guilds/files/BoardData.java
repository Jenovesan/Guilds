package com.guildwars.guildwars.guilds.files;

import com.guildwars.guildwars.guilds.GuildChunk;
import com.guildwars.guildwars.guilds.ObjectDataManager;

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

        String boardLocation = chunk.getBoardLocation().getX() + "," + chunk.getBoardLocation().getZ();

        chunkData.put("boardLocation", boardLocation);

        chunkData.put("guildId", chunk.getGuild().getId());

        saveRaw(boardLocation, chunkData);
    }
}
