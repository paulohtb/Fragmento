package com.pgalaxyp.fragmento.rpg.engine.intent;

import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import java.util.List;

public interface IntentSource {
    List<IntentEnvelope> drain();
}