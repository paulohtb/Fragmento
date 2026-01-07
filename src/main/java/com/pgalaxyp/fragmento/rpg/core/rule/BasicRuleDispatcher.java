package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionRuleSet;
import com.pgalaxyp.fragmento.rpg.core.rule.action.PendingTargeting;
import com.pgalaxyp.fragmento.rpg.core.rule.effect.EffectRule;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.InterruptIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.interrupt.InterruptRule;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.ArrayList;
import java.util.List;

public final class BasicRuleDispatcher implements RuleDispatcher {

    private final ActionRuleSet actionRules;
    private final InterruptRule interruptRule;
    private final EffectRule effectRule;

    public BasicRuleDispatcher(
            ActionRuleSet actionRules,
            InterruptRule interruptRule,
            EffectRule effectRule
    ) {
        this.actionRules = actionRules;
        this.interruptRule = interruptRule;
        this.effectRule = effectRule;
    }

    @Override
    public RuleFrame applyPrimary(ActionIntent intent, ActorState current, ActionDef action, long now) {
        var result = actionRules.applyPrimary(
                intent.actorId(),
                current,
                action,
                now
        );

        return new RuleFrame(
                result.nextState(),
                result.targetings(),
                List.of(),
                result.consumed()
        );
    }

    @Override
    public RuleFrame applyInterrupt(InterruptIntent intent, ActorState current, long now) {
        if (current == null) current = ActorState.empty(intent.actorId());

        var next = interruptRule.apply(
                intent.actorId(),
                current,
                intent.cause(),
                now
        );

        if (next.equals(current)) {
            return new RuleFrame(current, List.of(), List.of(), false);
        }

        return new RuleFrame(
                next,
                List.of(),
                List.of(),
                true
        );
    }

    @Override
    public RuleFrame resolveTargeting(
            RuleFrame previous,
            ActionDef action,
            PendingTargeting targeting,
            TargetingResolution resolution,
            long now
    ) {
        var eff = effectRule.onTargetResolved(
                targeting.actorId(),
                previous.nextState(),
                action,
                targeting.comboIndex(),
                resolution,
                now
        );

        var effects = new ArrayList<>(previous.effects());
        if (eff.effect() != null) effects.add(eff.effect());

        return new RuleFrame(
                eff.nextState(),
                List.of(),
                List.copyOf(effects),
                eff.consumed()
        );
    }
}