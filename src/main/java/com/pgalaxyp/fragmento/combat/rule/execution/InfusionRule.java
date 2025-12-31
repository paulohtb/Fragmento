package com.pgalaxyp.fragmento.combat.rule.execution;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class InfusionRule {

    public Result resolve(
            InfusionState state,
            CombatTime now
    ) {
        if (state == null || !state.isArmed()) {
            return Result.none(state);
        }

        if (state.isExpired(now)) {
            return Result.none(state.clear());
        }

        return Result.consume(state.clear(), state.spec().infusedAction());
    }

    public record Result(
            InfusionState nextState,
            ActionDefinition infusedAction
    ) {
        public static Result none(InfusionState state) {
            return new Result(state, null);
        }

        public static Result consume(InfusionState state, ActionDefinition action) {
            return new Result(state, action);
        }
    }
}