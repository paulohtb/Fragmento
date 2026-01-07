package com.pgalaxyp.fragmento.rpg.core.rule.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;

public interface EffectRule {

    EffectResult onTargetResolved(
            long actorId,
            ActorState current,
            ActionDef action,
            int comboIndex,
            TargetingResolution resolution,
            long now
    );

    record EffectResult(
            ActorState nextState,
            EffectState effect,
            boolean consumed
    ) {
        public static EffectResult ignored(ActorState state) {
            return new EffectResult(state, null, false);
        }
    }
}