package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboProgressionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.command.RequestTargeting;
import com.pgalaxyp.fragmento.rpg.core.rule.command.RuleCommand;
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

        var nextState = new ActorState(
                actorId,
                nextAction,
                nextCombo,
                current != null ? current.missiles() : null
        );

        List<RuleCommand> commands = List.of(new RequestTargeting(
                actorId,
                action.id(),
                index,
                step.id(),
                step.effect(),
                step.targeting(),
                20.0,
                10.0,
                now
        ));

        return new ActionResult(nextState, commands);
    }
}