package com.pgalaxyp.fragmento.rpg.core.rule.targeting;

import com.pgalaxyp.fragmento.rpg.core.domain.event.EffectAppliedEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingDataCollectedEvent;
import java.util.List;

public final class TargetingApplyRule {

    public List<RpgEvent> apply(TargetingDataCollectedEvent e, long now) {
        return e.target()
                .map(t -> List.<RpgEvent>of(new EffectAppliedEvent(
                        e.request().actorId(),
                        e.request().actionId(),
                        e.request().comboIndex(),
                        e.request().stepId(),
                        e.request().effect(),
                        t,
                        now
                )))
                .orElseGet(List::of);
    }
}