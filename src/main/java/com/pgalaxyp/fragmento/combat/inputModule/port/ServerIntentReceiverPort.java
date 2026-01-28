package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;

public interface ServerIntentReceiverPort {
    void enqueue(IntentEnvelope envelope);
}