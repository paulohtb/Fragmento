package com.pgalaxyp.fragmento.rpg.core.event.resolution;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;

public sealed interface DomainResolution permits TargetingQueryResolved {
    QueryId queryId();
}