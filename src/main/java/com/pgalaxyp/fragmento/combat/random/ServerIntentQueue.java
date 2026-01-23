package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.inputModule.port.IntentSourcePort;
import com.pgalaxyp.fragmento.combat.inputModule.port.ServerIntentReceiverPort;
import java.util.List;
import java.util.Objects;

public final class ServerIntentQueue implements IntentSourcePort, ServerIntentReceiverPort {
    private final IntentQueue queue;

    public ServerIntentQueue(IntentQueue queue) {
        this.queue = Objects.requireNonNull(queue);
    }

    @Override
    public void enqueue(IntentEnvelope envelope) {
        queue.push(Objects.requireNonNull(envelope));
    }

    @Override
    public List<IntentEnvelope> drain() {
        return queue.drain();
    }
}