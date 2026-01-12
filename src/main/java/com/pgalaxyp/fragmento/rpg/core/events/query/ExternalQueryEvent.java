package com.pgalaxyp.fragmento.rpg.core.events.query;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;

public sealed interface ExternalQueryEvent permits TargetingQueryRequested {
    QueryId queryId();
}