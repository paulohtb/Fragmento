package com.pgalaxyp.fragmento.rpg.core.rule.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequestedEvent;
import java.util.List;

public final class BasicEffectRule implements EffectRule {

    @Override
    public List<RpgEvent> onComboStep(long actorId, ActionDef action, int comboIndex, long now) {
        var steps = action.combo().steps();
        if (comboIndex < 0 || comboIndex >= steps.size()) return List.of();

        var step = steps.get(comboIndex);

        return List.of(new TargetingRequestedEvent(
                actorId,
                action.id(),
                comboIndex,
                step.id(),
                step.effect(),
                step.targeting(),
                now
        ));
    }
}