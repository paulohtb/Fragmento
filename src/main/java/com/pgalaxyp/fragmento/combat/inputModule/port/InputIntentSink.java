package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.random.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public interface InputIntentSink {
    void emit(IntentEnvelope envelope);

    default void emit(ActorId actorId, Object intent) {
        emit(IntentEnvelope.of(actorId, intent));
    }

    default void emit(ActorId actorId, Object intent, long clientFrameHint) {
        emit(IntentEnvelope.of(actorId, intent, clientFrameHint));
    }
}