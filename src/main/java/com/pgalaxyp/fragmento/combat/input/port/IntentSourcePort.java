package com.pgalaxyp.fragmento.combat.input.port;

import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import java.util.List;

public interface IntentSourcePort {
    List<IntentEnvelope> drain();
}