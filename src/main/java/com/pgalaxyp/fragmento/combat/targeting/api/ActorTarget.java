package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;

public record ActorTarget(ActorId actorId) implements Target {
    public ActorTarget { if (actorId == null) throw new IllegalArgumentException(); }
}