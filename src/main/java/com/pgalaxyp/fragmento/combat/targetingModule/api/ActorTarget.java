package com.pgalaxyp.fragmento.combat.targetingModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public record ActorTarget(ActorId actorId) implements Target {
    public ActorTarget { if (actorId == null) throw new IllegalArgumentException(); }
}