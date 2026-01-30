package com.pgalaxyp.fragmento.combat.intentModule;

import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;
import java.util.*;

public final class ServerIntentQueue implements IntentSourcePort, IntentSinkPort {
    private final IntentQueue queue;

    public ServerIntentQueue(IntentQueue queue) { this.queue = Objects.requireNonNull(queue); }

    @Override public void enqueue(IntentEnvelope envelope) { queue.enqueue(envelope); }
    @Override public List<IntentEnvelope> drain() { return queue.drain(); }
}