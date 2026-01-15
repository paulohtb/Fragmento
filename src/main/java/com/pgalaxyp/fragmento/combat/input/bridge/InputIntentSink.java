package com.pgalaxyp.fragmento.combat.input.bridge;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;

public interface InputIntentSink {

    void emit(IntentEnvelope envelope);

    default void emit(ActorId actorId, DomainIntent intent) {
        emit(IntentEnvelope.of(actorId, intent));
    }

    default void emit(ActorId actorId, DomainIntent intent, long clientFrameHint) {
        emit(IntentEnvelope.of(actorId, intent, clientFrameHint));
    }
}