package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import java.util.List;

public record EngineTickResult(
        CombatSnapshot snapshot,
        List<DomainEvent> events,
        List<DomainEvent> targetingRequests
) {}