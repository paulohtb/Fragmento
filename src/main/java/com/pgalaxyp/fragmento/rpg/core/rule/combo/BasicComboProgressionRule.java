package com.pgalaxyp.fragmento.rpg.core.rule.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public final class BasicComboProgressionRule implements ComboProgressionRule {

    @Override
    public int nextIndex(ActorState current, ActionDef action, long now) {
        var steps = action.combo().steps();
        var size = steps.size();
        if (size == 0) return 0;

        if (current == null) return 0;

        var combo = current.combo();
        if (combo == null || combo.actionId() == null) return 0;

        if (!combo.actionId().equals(action.id())) return 0;

        var candidate = combo.index() + 1;
        if (candidate >= size) return 0;
        return candidate;
    }
}