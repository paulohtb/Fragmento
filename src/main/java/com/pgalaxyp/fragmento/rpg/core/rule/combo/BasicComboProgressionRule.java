package com.pgalaxyp.fragmento.rpg.core.rule.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public final class BasicComboProgressionRule implements ComboProgressionRule {

    @Override
    public int nextIndex(ActorState current, ActionDef action, long now) {
        if (action.combo() == null || action.combo().size() == 0) return 0;
        if (current == null || !current.combo().matches(action.id())) return 0;

        int next = current.combo().index() + 1;
        return Math.min(next, action.combo().size() - 1);
    }
}