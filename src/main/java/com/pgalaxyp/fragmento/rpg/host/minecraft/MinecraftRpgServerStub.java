package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.engine.EngineFrameOutput;
import com.pgalaxyp.fragmento.rpg.engine.RpgEngine;
import com.pgalaxyp.fragmento.rpg.engine.intent.IntentQueue;
import java.util.List;

public final class MinecraftRpgServerStub {

    private final MinecraftHostStub host;
    private final IntentQueue queue;
    private final RpgEngine engine;

    public MinecraftRpgServerStub(MinecraftHostStub host, IntentQueue queue, RpgEngine engine) {
        if (host == null || queue == null || engine == null) {
            throw new IllegalArgumentException();
        }
        this.host = host;
        this.queue = queue;
        this.engine = engine;
    }

    public EngineFrameOutput tick(int tickIndex) {
        List<IntentEnvelope> captured = host.captureIntents();
        for (IntentEnvelope env : captured) {
            if (env == null) {
                throw new IllegalArgumentException();
            }
            queue.push(env);
        }

        EngineFrameOutput out = engine.step(tickIndex);

        List<DomainEvent> events = out.events();
        host.applyEvents(events);

        return out;
    }
}