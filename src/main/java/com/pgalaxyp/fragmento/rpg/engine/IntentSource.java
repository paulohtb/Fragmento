package com.pgalaxyp.fragmento.rpg.engine;

import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.DomainIntent;

public interface IntentSource {
    List<DomainIntent> drain();
}