package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.inputModule.port.IntentSourcePort;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IntentQueue implements IntentSourcePort {

    private final ConcurrentLinkedQueue<IntentEnvelope> queue = new ConcurrentLinkedQueue<>();

    public void push(IntentEnvelope envelope) {
        if (envelope == null) throw new IllegalArgumentException();
        queue.add(envelope);
    }

    @Override
    public List<IntentEnvelope> drain() {
        List<IntentEnvelope> out = new ArrayList<>();
        for (;;) {
            IntentEnvelope next = queue.poll();
            if (next == null) break;
            out.add(next);
        }
        return out;
    }
}