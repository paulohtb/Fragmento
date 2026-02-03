package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public record IntentEnvelope(ActorId actorId, Object intent) {
    public IntentEnvelope {
        if (actorId == null || intent == null) throw new IllegalArgumentException();
    }

    public static IntentEnvelope of(ActorId actorId, Object intent) {
        return new IntentEnvelope(actorId, intent);
    }
}