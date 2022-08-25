package com.guildwars.guildwars.guilds.engine;

public class EngineIntegration {
    public static void activateEngines() {
        new AutoClaim();
        new GuildRelations();
        new MapAuto();
        new PlayerChunkUpdate();
        new Power();
        new Raiding();
        new RegisterGPlayer();
    }
}
