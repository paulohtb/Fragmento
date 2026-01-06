package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.command.RuleCommand;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public interface RuleDispatcher {

    RuleFrame applyPrimary(
            ActionIntent intent,
            ActorState current,
            ActionDef action,
            long now
    );

    RuleFrame applyInterrupt(
            long actorId,
            ActorState current,
            long now
    );

    RuleFrame applyTargetingResult(
            RuleFrame previous,
            ActionDef action,
            TargetingResolution resolution,
            long now
    );

    record RuleFrame(
            ActorState nextState,
            List<RuleCommand> commands,
            boolean consumed
    ) {}
}