package com.pgalaxyp.fragmento.combat.engine.rules;

import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class InfusedSkillRules {

    public boolean canArm(InfusionState state) {
        return state != null && !state.isArmed();
    }

    public void arm(
            InfusionState state,
            InfusionSpec spec,
            CombatTime now
    ) {
        if (state == null || spec == null) {
            return;
        }
        state.arm(spec, now);
    }
}