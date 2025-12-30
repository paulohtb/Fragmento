package com.pgalaxyp.fragmento.combat.engine.rules;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class InfusionRules {

    public ActionDefinition resolveInfusedAction(
            InfusionState infusion,
            CombatTime now
    ) {
        if (infusion == null || !infusion.isArmed()) {
            return null;
        }
        if (infusion.isExpired(now)) {
            infusion.clear();
            return null;
        }
        return infusion.consume();
    }
}