package com.pgalaxyp.fragmento.rpg.core.rule.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public interface ComboProgressionRule {
    int nextIndex(ActorState current, ActionDef action, long now);
}