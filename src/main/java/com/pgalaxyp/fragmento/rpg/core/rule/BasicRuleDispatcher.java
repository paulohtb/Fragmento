package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionRuleSet;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.interrupt.InterruptRule;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.platform.api.content.ActionResolver;

import java.util.List;

public final class BasicRuleDispatcher implements RuleDispatcher {

    private final ActionResolver actionResolver;
    private final ActionRuleSet actionRules;
    private final InterruptRule interruptRule;

    public BasicRuleDispatcher(
            ActionResolver actionResolver,
            ActionRuleSet actionRules,
            InterruptRule interruptRule
    ) {
        this.actionResolver = actionResolver;
        this.actionRules = actionRules;
        this.interruptRule = interruptRule;
    }

    @Override
    public RuleFrame applyPrimary(ActionIntent intent, ActorState current, long now) {
        var actionOpt = actionResolver.resolve(
                intent.actorId(),
                intent.actionId()
        );

        if (actionOpt.isEmpty()) {
            return new RuleFrame(current, List.of(), false);
        }

        ActionDef action = actionOpt.get();

        var result = actionRules.applyPrimary(
                intent.actorId(),
                current,
                action,
                now
        );

        return new RuleFrame(
                result.nextState() != null ? result.nextState() : current,
                result.events(),
                result.consumed()
        );
    }

    @Override
    public RuleFrame applyInterrupt(long actorId, ActorState current, long now) {
        var result = interruptRule.apply(
                actorId,
                current,
                null,
                now
        );

        return new RuleFrame(
                result.nextState() != null ? result.nextState() : current,
                result.events(),
                result.consumed()
        );
    }
}