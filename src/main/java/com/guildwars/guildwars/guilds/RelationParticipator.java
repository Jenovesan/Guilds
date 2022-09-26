package com.guildwars.guildwars.guilds;

public interface RelationParticipator {
    Relation getRelationTo(RelationParticipator participator);
    String describe(RelationParticipator participator);
}
