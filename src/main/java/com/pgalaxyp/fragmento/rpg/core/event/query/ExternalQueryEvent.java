package com.pgalaxyp.fragmento.rpg.core.event.query;

public sealed interface ExternalQueryEvent permits TargetingQueryRequested {
    QueryId queryId();
}