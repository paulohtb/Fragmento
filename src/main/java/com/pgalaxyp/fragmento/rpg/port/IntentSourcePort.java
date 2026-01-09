package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import java.util.List;

public interface IntentSourcePort {
    List<IntentEnvelope> drain();
}