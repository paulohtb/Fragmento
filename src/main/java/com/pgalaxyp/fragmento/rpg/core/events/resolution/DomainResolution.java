package com.pgalaxyp.fragmento.rpg.core.events.resolution;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;

public sealed interface DomainResolution permits TargetingQueryResolved {
    QueryId queryId();
}