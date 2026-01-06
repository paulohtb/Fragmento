package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboProgressionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.effect.EffectRule;
import com.pgalaxyp.fragmento.rpg.core.rule.priority.PriorityRule;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;

public final class ActionOrchestrator {

    private final ActionTimingRule timingRule;
    private final PriorityRule priorityRule;
    private final ComboProgressionRule comboRule;
    private final EffectRule effectRule;

    public ActionOrchestrator(
            ActionTimingRule timingRule,
            PriorityRule priorityRule,
            ComboProgressionRule comboRule,
            EffectRule effectRule
    ) {
        this.timingRule = timingRule;
        this.priorityRule = priorityRule;
        this.comboRule = comboRule;
        this.effectRule = effectRule;
    }

    public ActionResult apply(long actorId, ActorState current, ActionDef action, long now) {
        var currentAction = current != null ? current.currentAction() : null;

        if (currentAction != null && currentAction.isActiveAt(now)) {
            if (!priorityRule.canInterrupt(currentAction, action)) {
                return ActionResult.ignored();
            }
        }

        var index = comboRule.nextIndex(current, action, now);
        var step = action.combo().steps().get(index);

        var endsAt = timingRule.computeEndsAt(step.timeline(), now);

        var nextAction = new ActionState(
                action.id(),
                action.priority(),
                now,
                endsAt,
                action.interruptMask()
        );

        var nextCombo = new ComboState(action.id(), index);
        var nextState = new ActorState(actorId, nextAction, nextCombo);

        var events = effectRule.onComboStep(actorId, action, index, now);

        return new ActionResult(nextState, events);
    }
}