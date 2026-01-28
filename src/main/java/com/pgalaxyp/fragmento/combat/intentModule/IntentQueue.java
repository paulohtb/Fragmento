package com.pgalaxyp.fragmento.combat.intentModule;

import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IntentQueue {
    private final ConcurrentLinkedQueue<IntentEnvelope> q = new ConcurrentLinkedQueue<>();

    public void enqueue(IntentEnvelope envelope) {
        q.add(Objects.requireNonNull(envelope));
    }

    public List<IntentEnvelope> drain() {
        if (q.isEmpty()) return List.of();
        var out = new ArrayList<IntentEnvelope>();
        for (IntentEnvelope e; (e = q.poll()) != null; ) out.add(e);
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}