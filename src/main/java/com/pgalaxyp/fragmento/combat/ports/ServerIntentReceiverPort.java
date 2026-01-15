package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.core.events.intent.IntentEnvelope;

public interface ServerIntentReceiverPort {
    void enqueue(IntentEnvelope envelope);
}