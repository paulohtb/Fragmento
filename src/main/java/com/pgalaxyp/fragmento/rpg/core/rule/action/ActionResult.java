package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.rule.command.RuleCommand;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public record ActionResult(
        ActorState nextState,
        List<RuleCommand> commands
) {
    public boolean consumed() {
        return commands != null && !commands.isEmpty();
    }

    public static ActionResult ignored() {
        return new ActionResult(null, List.of());
    }
}