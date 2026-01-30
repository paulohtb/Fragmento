package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;

public interface IntentSinkPort {
    void enqueue(IntentEnvelope envelope);

    default void enqueue(ActorId actorId, Object intent) {
        enqueue(IntentEnvelope.of(actorId, intent));
    }

    default void enqueue(ActorId actorId, Object intent, long clientFrameHint) {
        enqueue(IntentEnvelope.of(actorId, intent, clientFrameHint));
    }
}