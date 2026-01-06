package com.pgalaxyp.fragmento.rpg.platform.api.events;

import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequestedEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingDataCollectedEvent;

public interface WorldQueryGateway {
    TargetingDataCollectedEvent collect(TargetingRequestedEvent request);
}