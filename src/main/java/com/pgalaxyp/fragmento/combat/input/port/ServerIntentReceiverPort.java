package com.pgalaxyp.fragmento.combat.input.port;

import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;

public interface ServerIntentReceiverPort {
    void enqueue(IntentEnvelope envelope);
}