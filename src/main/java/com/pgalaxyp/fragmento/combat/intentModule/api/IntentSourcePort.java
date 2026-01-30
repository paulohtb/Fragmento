package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;
import java.util.List;

public interface IntentSourcePort {
    List<IntentEnvelope> drain();
}