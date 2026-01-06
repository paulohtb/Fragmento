package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public interface ActionRuleSet {

    ActionResult applyPrimary(
            long actorId,
            ActorState current,
            ActionDef action,
            long now
    );
}