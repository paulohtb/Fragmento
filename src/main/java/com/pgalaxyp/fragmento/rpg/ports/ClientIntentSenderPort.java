package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;

public interface ClientIntentSenderPort {
    void send(IntentEnvelope envelope);
}