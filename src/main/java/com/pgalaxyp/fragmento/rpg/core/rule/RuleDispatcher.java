package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.action.PendingTargeting;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.InterruptIntent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;

import java.util.List;

public interface RuleDispatcher {

    RuleFrame applyPrimary(
            ActionIntent intent,
            ActorState current,
            ActionDef action,
            long now
    );

    RuleFrame applyInterrupt(
            InterruptIntent intent,
            ActorState current,
            long now
    );

    RuleFrame resolveTargeting(
            RuleFrame previous,
            ActionDef action,
            PendingTargeting targeting,
            TargetingResolution resolution,
            long now
    );

    record RuleFrame(
            ActorState nextState,
            List<PendingTargeting> targetings,
            List<EffectState> effects,
            boolean consumed
    ) {}
}