package com.pgalaxyp.fragmento.rpg.core.event.query;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;

public sealed interface ExternalQueryEvent permits TargetingQueryRequested {
    QueryId queryId();
}