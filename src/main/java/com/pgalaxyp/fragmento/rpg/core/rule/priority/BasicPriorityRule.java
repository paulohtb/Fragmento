package com.pgalaxyp.fragmento.rpg.core.rule.priority;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.action.CancelPolicy;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;

public final class BasicPriorityRule implements PriorityRule {

    @Override
    public boolean canInterrupt(ActionState currentAction, ActionDef nextAction) {
        if (currentAction == null) return true;

        var policy = nextAction.cancelPolicy();
        if (policy == CancelPolicy.NONE) return false;

        int cmp = nextAction.priority().compareTo(currentAction.priority());
        if (policy == CancelPolicy.ANY) return true;
        return cmp >= 0;
    }
}