package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionRuleSet;
import com.pgalaxyp.fragmento.rpg.core.rule.command.RequestTargeting;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.interrupt.InterruptRule;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
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
                result.nextState() != null ? result.nextState() : current,
                result.commands(),
                result.consumed()
        );
    }

    @Override
    public RuleFrame applyInterrupt(long actorId, ActorState current, long now) {
        var result = interruptRule.apply(actorId, current, null, now);
        return new RuleFrame(
                result.nextState() != null ? result.nextState() : current,
                List.of(),
                result.consumed()
        );
    }

    @Override
    public RuleFrame applyTargetingResult(
            RuleFrame previous,
            ActionDef action,
            TargetingResolution resolution,
            long now
    ) {
        for (var cmd : previous.commands()) {
            if (cmd instanceof RequestTargeting rt) {
                var eff = effectRule.onTargetResolved(
                        rt.actorId(),
                        previous.nextState(),
                        action,
                        rt.comboIndex(),
                        resolution,
                        now
                );

                return new RuleFrame(eff.nextState(), List.of(), eff.consumed());
            }
        }

        return previous;
    }
}