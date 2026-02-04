package com.pgalaxyp.fragmento.combat.intentModule.api;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ServerIntentQueue implements IntentSourcePort, IntentSinkPort {
    private final ConcurrentLinkedQueue<IntentEnvelope> queue = new ConcurrentLinkedQueue<>();

    @Override public void enqueue(IntentEnvelope envelope) { queue.add(Objects.requireNonNull(envelope)); }

    @Override public List<IntentEnvelope> drain() {
        if (queue.isEmpty()) return List.of();
        var out = new ArrayList<IntentEnvelope>();
        for (IntentEnvelope e; (e = queue.poll()) != null; ) out.add(e);
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}