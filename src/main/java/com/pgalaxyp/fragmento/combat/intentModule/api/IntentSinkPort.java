package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public interface IntentSinkPort {
    boolean enqueue(IntentEnvelope envelope);
    default boolean enqueue(ActorId actorId, Object intent) { return enqueue(IntentEnvelope.of(actorId, intent)); }
}