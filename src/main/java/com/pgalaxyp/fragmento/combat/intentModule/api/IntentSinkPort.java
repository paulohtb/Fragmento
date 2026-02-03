package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public interface IntentSinkPort {
    void enqueue(IntentEnvelope envelope);
    default void enqueue(ActorId actorId, Object intent) { enqueue(IntentEnvelope.of(actorId, intent)); }
}