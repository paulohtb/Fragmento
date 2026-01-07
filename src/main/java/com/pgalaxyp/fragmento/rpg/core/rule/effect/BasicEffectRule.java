package com.pgalaxyp.fragmento.rpg.core.rule.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;

public final class BasicEffectRule implements EffectRule {

    @Override
    public EffectResult onTargetResolved(
            long actorId,
            ActorState current,
            ActionDef action,
            int comboIndex,
            TargetingResolution resolution,
            long now
    ) {
        if (current == null) current = ActorState.empty(actorId);
        if (action == null || action.combo() == null) return EffectResult.ignored(current);

        if (comboIndex < 0 || comboIndex >= action.combo().size()) {
            return EffectResult.ignored(current);
        }

        var step = action.combo().step(comboIndex);
        EffectId effect = step.effect();
        if (effect == null) {
            return EffectResult.ignored(current);
        }

        return new EffectResult(
                current,
                new EffectState(
                        actorId,
                        effect,
                        resolution.resolvedTarget(),
                        now
                ),
                true
        );
    }
}