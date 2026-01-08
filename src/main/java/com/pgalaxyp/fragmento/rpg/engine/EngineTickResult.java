package com.pgalaxyp.fragmento.rpg.engine;

import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;

public record EngineTickResult(
        GameSnapshot snapshot,
        List<DomainEvent> events
) {}