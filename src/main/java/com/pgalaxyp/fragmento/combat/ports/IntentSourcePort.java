package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.core.events.intent.IntentEnvelope;
import java.util.List;

public interface IntentSourcePort {
    List<IntentEnvelope> drain();
}