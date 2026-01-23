package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.random.IntentEnvelope;

public interface ServerIntentReceiverPort {
    void enqueue(IntentEnvelope envelope);
}