package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;

public interface ServerIntentReceiverPort {
    void enqueue(IntentEnvelope envelope);
}