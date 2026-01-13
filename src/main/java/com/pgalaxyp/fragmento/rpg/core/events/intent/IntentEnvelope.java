package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public record IntentEnvelope(ActorId actorId, DomainIntent intent, Long clientFrameHint) {

    public IntentEnvelope {
        if (actorId == null || intent == null) {
            throw new IllegalArgumentException();
        }
        if (clientFrameHint != null && clientFrameHint < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Optional<Long> clientFrameHintOpt() {
        return Optional.ofNullable(clientFrameHint);
    }

    public static IntentEnvelope of(ActorId actorId, DomainIntent intent) {
        return new IntentEnvelope(actorId, intent, null);
    }

    public static IntentEnvelope of(ActorId actorId, DomainIntent intent, long clientFrameHint) {
        return new IntentEnvelope(actorId, intent, clientFrameHint);
    }
}