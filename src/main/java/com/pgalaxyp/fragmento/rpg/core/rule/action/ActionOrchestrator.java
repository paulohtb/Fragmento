package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboProgressionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.priority.PriorityRule;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;
import java.util.List;

public final class ActionOrchestrator implements ActionRuleSet {

    private final PriorityRule priorityRule;
    private final ComboProgressionRule comboRule;
    private final ActionTimingRule timingRule;

    public ActionOrchestrator(
            PriorityRule priorityRule,
            ComboProgressionRule comboRule,
            ActionTimingRule timingRule
    ) {
        this.priorityRule = priorityRule;
        this.comboRule = comboRule;
        this.timingRule = timingRule;
    }

    @Override
    public ActionResult applyPrimary(long actorId, ActorState current, ActionDef action, long now) {
        var base = current == null ? ActorState.empty(actorId) : current;

        var running = base.currentAction();
        if (running != null && running.isActiveAt(now)) {
            if (!priorityRule.canInterrupt(running, action)) {
                return ActionResult.ignored(base);
            }
        }

        if (!action.hasCombo()) {
            return ActionResult.ignored(base);
        }

        int nextIndex = comboRule.nextIndex(base, action, now);
        var step = action.combo().step(nextIndex);

        long endsAt = timingRule.computeEndsAt(step.timeline(), now);

        var nextAction = new ActionState(
                action.id(),
                action.priority(),
                now,
                endsAt,
                step.interruptMask()
        );

        var nextCombo = new ComboState(action.id(), nextIndex);

        var nextState = base.with(nextAction, nextCombo);

        var targeting = new PendingTargeting(
                actorId,
                action.id(),
                nextIndex,
                step.effect(),
                step.targeting(),
                20.0,
                10.0,
                now
        );

        return ActionResult.applied(nextState, List.of(targeting));
    }
}