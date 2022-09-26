package com.guildwars.guildwars.guilds.engine;

public class EngineIntegration {
    public static void activateEngines() {
        new AutoClaim();
        new MapAuto();
        new PlayerChunkUpdate();
        new Power();
        new Raiding();
        new RegisterGPlayer();
        new NewTerritoryInform();
        new ChangeGuildLeadershipOnLeaderBan();
        new Home();
        new InviteEngine();
        new IndexingEngine();
    }
}
