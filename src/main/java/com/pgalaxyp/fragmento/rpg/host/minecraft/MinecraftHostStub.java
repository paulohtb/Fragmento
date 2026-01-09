package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import java.util.List;

public final class MinecraftHostStub {

    public List<IntentEnvelope> captureIntents() {
        throw new UnsupportedOperationException();
    }

    public void applyEvents(List<DomainEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }
}