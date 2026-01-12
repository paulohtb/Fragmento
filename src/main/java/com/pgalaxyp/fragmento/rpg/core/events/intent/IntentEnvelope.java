package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import java.util.OptionalLong;

public record IntentEnvelope(
        ActorId actorId,
        DomainIntent intent,
        OptionalLong clientFrameHint
) {
    public IntentEnvelope {
        if (actorId == null || intent == null || clientFrameHint == null) {
            throw new IllegalArgumentException();
        }
        if (clientFrameHint.isPresent() && clientFrameHint.getAsLong() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static IntentEnvelope of(ActorId actorId, DomainIntent intent) {
        return new IntentEnvelope(actorId, intent, OptionalLong.empty());
    }
}