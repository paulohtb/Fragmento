package com.pgalaxyp.fragmento.combat.input.bridge;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.intent.*;

public interface InputIntentSink {

    void emit(IntentEnvelope envelope);

    default void emit(ActorId actorId, DomainIntent intent) {
        emit(IntentEnvelope.of(actorId, intent));
    }

    default void emit(ActorId actorId, DomainIntent intent, long clientFrameHint) {
        emit(IntentEnvelope.of(actorId, intent, clientFrameHint));
    }
}