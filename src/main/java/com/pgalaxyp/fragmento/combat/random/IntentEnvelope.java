package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Optional;

public record IntentEnvelope(ActorId actorId, Object intent, Long clientFrameHint) {
    public IntentEnvelope {
        if (actorId == null || intent == null) { throw new IllegalArgumentException(); }
        if (clientFrameHint != null && clientFrameHint < 0) { throw new IllegalArgumentException(); }
    }

    public Optional<Long> clientFrameHintOpt() {
        return Optional.ofNullable(clientFrameHint);
    }

    public static IntentEnvelope of(ActorId actorId, Object intent) {
        return new IntentEnvelope(actorId, intent, null);
    }

    public static IntentEnvelope of(ActorId actorId, Object intent, long clientFrameHint) {
        return new IntentEnvelope(actorId, intent, clientFrameHint);
    }
}