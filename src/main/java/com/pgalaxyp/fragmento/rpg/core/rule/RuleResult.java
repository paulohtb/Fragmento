package com.pgalaxyp.fragmento.rpg.core.rule;

import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;

public record RuleResult(
        List<StateDelta> deltas,
        List<DomainEvent> events
) {}