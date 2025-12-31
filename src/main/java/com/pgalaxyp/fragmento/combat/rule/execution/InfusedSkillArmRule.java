package com.pgalaxyp.fragmento.combat.rule.execution;

import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class InfusedSkillArmRule {

    public boolean canArm(InfusionState state) {
        return state == null || !state.isArmed();
    }

    public InfusionState arm(
            InfusionState state,
            InfusionSpec spec,
            CombatTime now
    ) {
        InfusionState base = state != null ? state : InfusionState.empty();
        return base.arm(spec, now);
    }
}