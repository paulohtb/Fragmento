package com.pgalaxyp.fragmento.combat.intentModule.api;

import java.util.List;

public interface IntentSourcePort {
    List<IntentEnvelope> drain();
}