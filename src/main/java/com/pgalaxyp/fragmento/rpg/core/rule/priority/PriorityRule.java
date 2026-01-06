package com.pgalaxyp.fragmento.rpg.core.rule.priority;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;

public interface PriorityRule {
    boolean canInterrupt(ActionState currentAction, ActionDef nextAction);
}