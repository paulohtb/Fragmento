package com.pgalaxyp.fragmento.rpg.input.bridge;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import java.util.OptionalLong;

public interface IntentEmitter {
    void emit(IntentEnvelope envelope);

    default void emit(ActorId actorId, DomainIntent intent, OptionalLong clientFrameHint) {
        if (actorId == null || intent == null || clientFrameHint == null) {
            throw new IllegalArgumentException();
        }
        emit(new IntentEnvelope(actorId, intent, clientFrameHint));
    }
}