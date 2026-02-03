package com.pgalaxyp.fragmento.combat.intentModule.api;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IntentQueue {
    private final ConcurrentLinkedQueue<IntentEnvelope> queue = new ConcurrentLinkedQueue<>();

    public void enqueue(IntentEnvelope envelope) {
        queue.add(Objects.requireNonNull(envelope));
    }

    public List<IntentEnvelope> drain() {
        if (queue.isEmpty()) return List.of();
        var out = new ArrayList<IntentEnvelope>();
        for (IntentEnvelope envelope; (envelope = queue.poll()) != null; ) out.add(envelope);
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}