package com.pgalaxyp.fragmento.rpg.engine.intent;

import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.port.IntentSourcePort;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class IntentQueue implements IntentSourcePort {

    private final Deque<IntentEnvelope> queue = new ArrayDeque<>();

    public void push(IntentEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        queue.addLast(envelope);
    }

    @Override
    public List<IntentEnvelope> drain() {
        List<IntentEnvelope> out = new ArrayList<>();
        while (!queue.isEmpty()) {
            out.add(queue.removeFirst());
        }
        return out;
    }
}