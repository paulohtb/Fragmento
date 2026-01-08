package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.core.domain.event.VisualEffectRequested;

public interface VisualEffectSink {
    void accept(VisualEffectRequested event);
}